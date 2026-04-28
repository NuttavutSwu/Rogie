package com.rogie.threekingdoms.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.CycleInterpolator
import android.view.animation.DecelerateInterpolator
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
import com.rogie.threekingdoms.model.CardType
import com.rogie.threekingdoms.model.Relic

class BattleActivity : AppCompatActivity() {
    private val viewModel: BattleViewModel by viewModels()
    private lateinit var cardAdapter: CardAdapter

    private lateinit var tvPlayerHp: TextView
    private lateinit var tvEnemyHp: TextView
    private lateinit var tvEnemyName: TextView
    private lateinit var tvCharacterStats: TextView
    private lateinit var tvEnergy: TextView
    private lateinit var tvGold: TextView
    private lateinit var tvStatus: TextView
    private lateinit var llPlayerHearts: LinearLayout
    private lateinit var llEnemyHearts: LinearLayout
    private lateinit var ivPlayerImage: ImageView
    private lateinit var ivEnemyImage: ImageView
    private lateinit var tvHonorCount: TextView
    private lateinit var tvRageCount: TextView
    private lateinit var tvInsightCount: TextView
    private lateinit var llRelics: LinearLayout
    private lateinit var tvRelicDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle)

        bindViews()
        setupRecycler()
        setupObservers()

        findViewById<Button>(R.id.btnEndTurn).setOnClickListener {
            playEnemyAttackAnimation {
                viewModel.endTurn()
                refreshStats()
                processBattleState()
                playPlayerHitAnimation()
            }
        }

        viewModel.setupBattle()
        setVisuals()
        refreshStats()
    }

    private fun bindViews() {
        tvPlayerHp = findViewById(R.id.tvPlayerHp)
        tvEnemyHp = findViewById(R.id.tvEnemyHp)
        tvEnemyName = findViewById(R.id.tvEnemyName)
        tvCharacterStats = findViewById(R.id.tvCharacterStats)
        tvEnergy = findViewById(R.id.tvEnergy)
        tvGold = findViewById(R.id.tvGold)
        tvStatus = findViewById(R.id.tvStatus)
        llPlayerHearts = findViewById(R.id.llPlayerHearts)
        llEnemyHearts = findViewById(R.id.llEnemyHearts)
        ivPlayerImage = findViewById(R.id.ivPlayerImage)
        ivEnemyImage = findViewById(R.id.ivEnemyImage)
        tvHonorCount = findViewById(R.id.tvHonorCount)
        tvRageCount = findViewById(R.id.tvRageCount)
        tvInsightCount = findViewById(R.id.tvInsightCount)
        llRelics = findViewById(R.id.llRelics)
        tvRelicDescription = findViewById(R.id.tvRelicDescription)
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

        // ปรับการดึงรูปศัตรูให้หลากหลายขึ้น
        val enemyName = viewModel.enemyNameText()
        val enemyRes = when {
            enemyName.contains("ลิโป้") || enemyName.contains("Lu Bu") -> R.drawable.img_character_lubu
            enemyName.contains("โจโฉ") || enemyName.contains("Cao Cao") -> R.drawable.img_character_caocao
            enemyName.contains("โจร") || enemyName.contains("Bandit") -> R.drawable.img_enemy_bandit
            enemyName.contains("ทัพหน้า") || enemyName.contains("Army") || enemyName.contains("Vanguard") -> R.drawable.img_enemy_army
            enemyName.contains("แม่ทัพ") || enemyName.contains("General") -> R.drawable.img_enemy_general
            else -> R.drawable.img_enemy_generic
        }
        ivEnemyImage.setImageResource(enemyRes)

        tvHonorCount.visibility = if (GameSession.selectedCharacter == CharacterId.GUAN_YU) View.VISIBLE else View.GONE
        tvRageCount.visibility = if (GameSession.selectedCharacter == CharacterId.LU_BU) View.VISIBLE else View.GONE
        tvInsightCount.visibility = if (GameSession.selectedCharacter == CharacterId.ZHUGE_LIANG) View.VISIBLE else View.GONE
    }

    private fun setupRecycler() {
        cardAdapter = CardAdapter { card ->
            val hasEnergy = GameSession.player.energy >= card.energyCost
            
            if (!hasEnergy) {
                playTiredAnimation()
                viewModel.playCard(card)
                return@CardAdapter
            }

            if (card.type == CardType.ATTACK) {
                playAttackAnimation {
                    viewModel.playCard(card)
                    refreshStats()
                    processBattleState()
                    playEnemyHitAnimation()
                }
            } else {
                viewModel.playCard(card)
                refreshStats()
                processBattleState()
            }
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

    private fun playAttackAnimation(onStrike: () -> Unit) {
        val originalX = ivPlayerImage.translationX
        val distance = (ivEnemyImage.x - ivPlayerImage.x) * 0.7f

        ivPlayerImage.animate()
            .translationXBy(distance)
            .setDuration(250)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    onStrike()
                    ivPlayerImage.animate()
                        .translationX(originalX)
                        .setDuration(400)
                        .setInterpolator(DecelerateInterpolator())
                        .setListener(null)
                        .start()
                }
            })
            .start()
    }

    private fun playEnemyAttackAnimation(onStrike: () -> Unit) {
        val originalX = ivEnemyImage.translationX
        val distance = (ivEnemyImage.x - ivPlayerImage.x) * 0.7f

        ivEnemyImage.animate()
            .translationXBy(-distance)
            .setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    onStrike()
                    ivEnemyImage.animate()
                        .translationX(originalX)
                        .setDuration(450)
                        .setInterpolator(DecelerateInterpolator())
                        .setListener(null)
                        .start()
                }
            })
            .start()
    }

    private fun playTiredAnimation() {
        ivPlayerImage.animate()
            .translationYBy(20f)
            .setDuration(150)
            .setInterpolator(CycleInterpolator(1f))
            .start()
        
        ivPlayerImage.animate()
            .rotationBy(5f)
            .setDuration(200)
            .setInterpolator(CycleInterpolator(1f))
            .start()
    }

    private fun playEnemyHitAnimation() {
        ivEnemyImage.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(50)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    ivEnemyImage.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(100)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                ivEnemyImage.animate()
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .setDuration(100)
                                    .setListener(null)
                                    .start()
                            }
                        })
                        .start()
                }
            })
            .start()
    }

    private fun playPlayerHitAnimation() {
        ivPlayerImage.animate()
            .scaleX(0.85f)
            .scaleY(0.85f)
            .setDuration(60)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    ivPlayerImage.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(120)
                        .start()
                }
            })
            .start()
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
        tvPlayerHp.text = viewModel.playerHpText()
        tvEnemyHp.text = viewModel.enemyHpText()
        tvEnemyName.text = viewModel.enemyNameText()
        tvCharacterStats.text = viewModel.characterStatsText()
        tvEnergy.text = viewModel.playerEnergyText()
        tvGold.text = "Gold: ${viewModel.playerGoldText()}"
        
        tvHonorCount.text = "Honor: ${GameSession.player.honor}"
        tvRageCount.text = "Rage: ${GameSession.player.rage}"
        tvInsightCount.text = "Insight: ${GameSession.player.insight}"
        
        renderHearts(llPlayerHearts, viewModel.playerHpValue(), viewModel.playerMaxHpValue())
        renderHearts(llEnemyHearts, viewModel.enemyHpValue(), viewModel.enemyMaxHpValue())
        renderRelics()
    }

    private fun renderRelics() {
        llRelics.removeAllViews()
        val inflater = LayoutInflater.from(this)
        GameSession.player.relics.forEach { relic ->
            val view = inflater.inflate(R.layout.item_relic, llRelics, false)
            // หาไอคอนภายใน View (ถ้ามี) หรือเซ็ต Background
            val ivRelicIcon = view.findViewById<ImageView>(R.id.ivRelicIcon)
            relic.iconResId?.let { ivRelicIcon?.setImageResource(it) }

            view.setOnClickListener {
                tvRelicDescription.text = "${relic.name}: ${relic.description}"
                tvRelicDescription.visibility = View.VISIBLE
                view.removeCallbacks(hideRelicDescRunnable)
                view.postDelayed(hideRelicDescRunnable, 3000)
            }
            llRelics.addView(view)
        }
    }

    private val hideRelicDescRunnable = Runnable {
        tvRelicDescription.visibility = View.INVISIBLE
    }

    private fun renderHearts(container: LinearLayout, currentHp: Int, maxHp: Int) {
        container.removeAllViews()
        repeat(maxHp) { index ->
            val heart = ImageView(this)
            val drawable = if (index < currentHp) R.drawable.ic_heart_full else R.drawable.ic_heart_empty
            heart.setImageResource(drawable)
            val size = resources.getDimensionPixelSize(R.dimen.heart_size_small)
            val params = LinearLayout.LayoutParams(size, size)
            params.marginEnd = 2
            heart.layoutParams = params
            container.addView(heart)
        }
    }

    private fun processBattleState() {
        if (viewModel.battleEnded.value == true) {
            if (GameSession.player.hp <= 0) {
                val earned = (GameSession.enemiesDefeated * 2) + (GameSession.bossesDefeated * 15) + (GameSession.currentChapter * 5)
                MetaProgressionManager.addSkillPoints(this, earned)
                startActivity(
                    Intent(this, DeathSummaryActivity::class.java)
                        .putExtra("earned_points", earned)
                        .putExtra("enemies_defeated", GameSession.enemiesDefeated)
                        .putExtra("bosses_defeated", GameSession.bossesDefeated)
                        .putExtra("chapter_reached", GameSession.currentChapter)
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
