package com.rogie.threekingdoms.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.game.CardLibrary
import com.rogie.threekingdoms.game.GameSession
import com.rogie.threekingdoms.model.Card
import com.rogie.threekingdoms.model.Relic
import com.rogie.threekingdoms.model.EffectType
import kotlin.random.Random

class RewardActivity : AppCompatActivity() {
    private lateinit var choices: List<Card>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)

        choices = CardLibrary.rewardPool().shuffled().take(3)

        bindCardChoice(R.id.btnRewardCard1, R.id.tvRewardCard1, choices[0])
        bindCardChoice(R.id.btnRewardCard2, R.id.tvRewardCard2, choices[1])
        bindCardChoice(R.id.btnRewardCard3, R.id.tvRewardCard3, choices[2])

        findViewById<Button>(R.id.btnSkipReward).setOnClickListener {
            val gotRelic = applySkipReward()
            if (gotRelic != null) {
                Toast.makeText(this, "ท่านได้รับวัตถุโบราณ: ${gotRelic.name}!", Toast.LENGTH_LONG).show()
                // Small delay to let player see the toast before moving
                it.postDelayed({ moveToRandomEncounter() }, 1000)
            } else {
                moveToRandomEncounter()
            }
        }
    }

    private fun bindCardChoice(buttonId: Int, textId: Int, card: Card) {
        findViewById<TextView>(textId).text = "${card.name}\n${card.description}"
        findViewById<Button>(buttonId).setOnClickListener {
            GameSession.addCard(card)
            applySecondaryReward()
            moveToRandomEncounter()
        }
    }

    private fun applySecondaryReward() {
        if (Random.nextBoolean()) {
            GameSession.player.gold += 15
        } else {
            GameSession.player.hp = minOf(GameSession.player.maxHp, GameSession.player.hp + 1)
        }
    }

    private fun applySkipReward(): Relic? {
        // 20% chance to get a relic when skipping
        if (Random.nextInt(100) < 20) {
            val newRelic = Relic("RELIC_SKIP", "ตราประทับโบราณ", "เพิ่มพลังโจมตีถาวร", EffectType.BUFF_STRENGTH, 1)
            
            if (GameSession.player.relics.size < 3) {
                GameSession.player.relics.add(newRelic)
                return newRelic
            } else {
                // If full, we could trigger a replace UI, but for now just add if space exists
                // Or inform the player it was full
                return null 
            }
        }
        
        // Always give some gold if no relic
        GameSession.player.gold += 20
        return null
    }

    private fun moveToRandomEncounter() {
        startActivity(Intent(this, StoryActivity::class.java))
        finish()
    }
}
