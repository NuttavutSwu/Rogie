package com.rogie.threekingdoms.ui

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.game.GameSession
import com.rogie.threekingdoms.game.ShopLibrary
import com.rogie.threekingdoms.model.StoryLibrary

/**
 * StoryActivity handles the narrative and event choices throughout the game.
 * It dynamically generates choice boxes and handles transitions between story and combat.
 */
class StoryActivity : AppCompatActivity() {

    private var isFightTriggered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        val event = when {
            GameSession.lastBossDefeatedId != null -> {
                val bossId = GameSession.lastBossDefeatedId!!
                GameSession.lastBossDefeatedId = null
                StoryLibrary.getBossPostChoice(bossId)
            }
            GameSession.needsEpilogue -> {
                GameSession.needsEpilogue = false
                StoryLibrary.getChapterEnd(GameSession.currentChapter - 1)
            }
            GameSession.isMainStoryEvent -> {
                GameSession.isMainStoryEvent = false
                ShopLibrary.resetChapterShop()
                StoryLibrary.getChapterStart(GameSession.currentChapter)
            }
            !ShopLibrary.shopEventSpawned && (GameSession.encounterCount % 8 >= 4) -> {
                ShopLibrary.shopEventSpawned = true
                StoryLibrary.getShopStoryEvent()
            }
            else -> StoryLibrary.getRandomEvent()
        }
        
        findViewById<TextView>(R.id.tvStoryTitle).text = event.title
        findViewById<TextView>(R.id.tvStoryDescription).text = event.description
        findViewById<ImageView>(R.id.ivEventImage).setImageResource(event.imageRes)

        val container = findViewById<LinearLayout>(R.id.llOptionsContainer)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val btnContinue = findViewById<View>(R.id.btnContinue)

        event.options.forEach { option ->
            // Use TextView with bg_option_box for all player choices
            val choiceBox = TextView(this).apply {
                text = option.text
                setTextColor(resources.getColor(R.color.scroll_ink, theme))
                textSize = 18f
                gravity = Gravity.CENTER
                setBackgroundResource(R.drawable.bg_option_box)
                
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 12, 0, 12)
                layoutParams = params
                
                setPadding(32, 24, 32, 24)
                isClickable = true
                isFocusable = true
                
                setOnClickListener {
                    option.onChoice(GameSession.player)
                    
                    if (option.isSpare || option.isExecute) {
                        GameSession.currentChapter++
                    }

                    tvResult.text = option.resultText
                    tvResult.visibility = View.VISIBLE
                    container.visibility = View.GONE
                    btnContinue.visibility = View.VISIBLE
                    
                    if (option.triggerFight && option.forcedEnemy != null) {
                        GameSession.setForcedEnemy(option.forcedEnemy)
                        isFightTriggered = true
                    } else {
                        isFightTriggered = false
                    }
                }
            }
            container.addView(choiceBox)
        }

        btnContinue.setOnClickListener {
            if (isFightTriggered) {
                startActivity(Intent(this, BattleActivity::class.java))
                finish()
            } else {
                val intent = Intent(this, StoryActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
