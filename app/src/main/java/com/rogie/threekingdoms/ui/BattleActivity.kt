package com.rogie.threekingdoms.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.game.GameSession
import com.rogie.threekingdoms.meta.CharacterId
import com.rogie.threekingdoms.meta.CharacterLibrary
import com.rogie.threekingdoms.meta.MetaProgressionManager

class BattleActivity : AppCompatActivity() {
    private val viewModel: BattleViewModel by viewModels()
    private lateinit var cardAdapter: CardAdapter

    private lateinit var tvPlayerHp: TextView
    private lateinit var tvEnemyHp: TextView
    private lateinit var tvEnemyHpNumeric: TextView
    private lateinit var tvEnemyName: TextView
    private lateinit var tvCharacterStats: TextView
    private lateinit var tvEnergy: TextView
    private lateinit var tvGold: TextView
    private lateinit var tvStatus: TextView
    private lateinit var llPlayerHearts: LinearLayout
    private lateinit var llEnemyHearts: LinearLayout
    private lateinit var ivPlayerImage: ImageView
    private lateinit var ivEnemyImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle)

        bindViews()
        setupRecycler()
        setupObservers()

        findViewById<Button>(R.id.btnEndTurn).setOnClickListener {
            viewModel.endTurn()
            refreshStats()
            processBattleState()
        }

        viewModel.setupBattle()
        setVisuals()
        refreshStats()
    }

    private fun bindViews() {
        tvPlayerHp = findViewById(R.id.tvPlayerHp)
        tvEnemyHp = findViewById(R.id.tvEnemyHp)
        tvEnemyHpNumeric = findViewById(R.id.tvEnemyHpNumeric)
        tvEnemyName = findViewById(R.id.tvEnemyName)
        tvCharacterStats = findViewById(R.id.tvCharacterStats)
        tvEnergy = findViewById(R.id.tvEnergy)
        tvGold = findViewById(R.id.tvGold)
        tvStatus = findViewById(R.id.tvStatus)
        llPlayerHearts = findViewById(R.id.llPlayerHearts)
        llEnemyHearts = findViewById(R.id.llEnemyHearts)
        ivPlayerImage = findViewById(R.id.ivPlayerImage)
        ivEnemyImage = findViewById(R.id.ivEnemyImage)
    }

    private fun setVisuals() {
        val playerRes = when (GameSession.selectedCharacter) {
            CharacterId.GUAN_YU -> R.drawable.img_character_guanyu
            CharacterId.ZHUGE_LIANG -> R.drawable.img_character_zhugeliang
            CharacterId.CAO_CAO -> R.drawable.img_character_caocao
            CharacterId.ZHOU_YU -> R.drawable.img_character_zhouyu
            CharacterId.LU_BU -> R.drawable.img_character_lubu
        }
        ivPlayerImage.setImageResource(playerRes)

        val enemyId = GameSession.buildEnemy().id // This is a bit hacky because buildEnemy is called in setupBattle too
        // Actually, we should get the current enemy from GameSession if it was saved there.
        // For now, let's use a generic image for non-bosses and specific ones for bosses.
        val enemyName = viewModel.enemyNameText()
        val enemyRes = when {
            enemyName.contains("Lu Bu") -> R.drawable.img_character_lubu
            enemyName.contains("Cao Cao") -> R.drawable.img_character_caocao
            else -> R.drawable.img_enemy_generic
        }
        ivEnemyImage.setImageResource(enemyRes)
    }

    private fun setupRecycler() {
        cardAdapter = CardAdapter { card ->
            viewModel.playCard(card)
            refreshStats()
            processBattleState()
        }

        findViewById<RecyclerView>(R.id.rvHand).apply {
            layoutManager = LinearLayoutManager(
                this@BattleActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = cardAdapter
        }
    }

    private fun setupObservers() {
        viewModel.hand.observe(this) {
            cardAdapter.update(it)
        }
        viewModel.status.observe(this) {
            tvStatus.text = it
        }
    }

    private fun refreshStats() {
        tvPlayerHp.text = getString(R.string.player_hp_fmt, viewModel.playerHpText())
        tvEnemyHp.text = getString(R.string.enemy_hp_fmt, viewModel.enemyHpText())
        tvEnemyHpNumeric.text = getString(R.string.enemy_hp_raw_fmt, viewModel.enemyNumericHpText())
        tvEnemyName.text = viewModel.enemyNameText()
        tvCharacterStats.text = getString(R.string.character_stats_fmt, viewModel.characterStatsText())
        tvEnergy.text = getString(R.string.energy_fmt, viewModel.playerEnergyText())
        tvGold.text = getString(R.string.gold_fmt, viewModel.playerGoldText())
        renderHearts(llPlayerHearts, viewModel.playerHpValue(), viewModel.playerMaxHpValue())
        renderHearts(llEnemyHearts, viewModel.enemyHpValue(), viewModel.enemyMaxHpValue())
    }

    private fun renderHearts(container: LinearLayout, currentHp: Int, maxHp: Int) {
        container.removeAllViews()
        val maxHearts = maxHp / 20
        val fullHearts = currentHp / 20
        val hasHalf = (currentHp % 20) >= 10
        repeat(maxHearts) { index ->
            val heart = ImageView(this)
            val drawable = when {
                index < fullHearts -> R.drawable.ic_heart_full
                index == fullHearts && hasHalf -> R.drawable.ic_heart_half
                else -> R.drawable.ic_heart_empty
            }
            heart.setImageResource(drawable)
            val params = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.heart_size),
                resources.getDimensionPixelSize(R.dimen.heart_size)
            )
            params.marginEnd = resources.getDimensionPixelSize(R.dimen.heart_gap)
            heart.layoutParams = params
            container.addView(heart)
        }
    }

    private fun processBattleState() {
        if (viewModel.battleEnded.value == true) {
            if (GameSession.player.hp <= 0) {
                val earned = (GameSession.enemiesDefeated * 2) + (GameSession.bossesDefeated * 15) + (GameSession.chapterReached * 5)
                MetaProgressionManager.addSkillPoints(this, earned)
                startActivity(
                    Intent(this, DeathSummaryActivity::class.java)
                        .putExtra("earned_points", earned)
                        .putExtra("enemies_defeated", GameSession.enemiesDefeated)
                        .putExtra("bosses_defeated", GameSession.bossesDefeated)
                        .putExtra("chapter_reached", GameSession.chapterReached)
                )
                finish()
                return
            }
            GameSession.pendingUnlock?.let { pending ->
                val unlocked = MetaProgressionManager.unlockCharacter(this, pending)
                if (unlocked) {
                    Toast.makeText(this, "${CharacterLibrary.get(pending).displayName} unlocked!", Toast.LENGTH_LONG).show()
                }
                GameSession.pendingUnlock = null
            }
            startActivity(Intent(this, RewardActivity::class.java))
            finish()
        }
    }
}
