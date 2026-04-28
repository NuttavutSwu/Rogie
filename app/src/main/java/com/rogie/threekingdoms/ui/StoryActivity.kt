package com.rogie.threekingdoms.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.game.GameSession
import com.rogie.threekingdoms.game.ShopLibrary
import com.rogie.threekingdoms.model.StoryLibrary

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
            // Use MaterialButton with TextButton style to remove the "Box" look
            val button = MaterialButton(this, null, com.google.android.material.R.attr.borderlessButtonStyle).apply {
                text = option.text
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 4, 0, 4)
                layoutParams = params
                
                // Clean look: No background box, just text
                setTextColor(getColor(R.color.scroll_ink))
                textAllCaps = false
                textSize = 18f
                
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
            container.addView(button)
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
