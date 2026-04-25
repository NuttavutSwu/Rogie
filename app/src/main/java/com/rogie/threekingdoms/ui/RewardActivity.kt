package com.rogie.threekingdoms.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.game.CardLibrary
import com.rogie.threekingdoms.game.GameSession
import com.rogie.threekingdoms.model.Card
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
            applySecondaryReward()
            moveToNextBattle()
        }
    }

    private fun bindCardChoice(buttonId: Int, textId: Int, card: Card) {
        findViewById<TextView>(textId).text = "${card.name}\n${card.description}"
        findViewById<Button>(buttonId).setOnClickListener {
            GameSession.addCard(card)
            applySecondaryReward()
            moveToNextBattle()
        }
    }

    private fun applySecondaryReward() {
        if (Random.nextBoolean()) {
            GameSession.player.gold += 15
        } else {
            GameSession.player.hp = minOf(GameSession.player.maxHp, GameSession.player.hp + 8)
        }

        // Optional roguelike relic. Keeps prototype expandable.
        if (!GameSession.hasImperialSeal && Random.nextInt(100) < 20) {
            GameSession.hasImperialSeal = true
        }
    }

    private fun moveToNextBattle() {
        startActivity(Intent(this, BattleActivity::class.java))
        finish()
    }
}
