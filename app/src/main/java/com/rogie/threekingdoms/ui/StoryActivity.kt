package com.rogie.threekingdoms.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
            // 1. ถ้าเพิ่งปราบบอส -> เลือกชะตากรรม (Spare/Execute)
            GameSession.lastBossDefeatedId != null -> {
                val bossId = GameSession.lastBossDefeatedId!!
                GameSession.lastBossDefeatedId = null
                StoryLibrary.getBossPostChoice(bossId)
            }
            // 2. ถ้าต้องการเนื้อเรื่องจบบท (Epilogue)
            GameSession.needsEpilogue -> {
                GameSession.needsEpilogue = false
                StoryLibrary.getChapterEnd(GameSession.currentChapter - 1)
            }
            // 3. ถ้าเริ่มบทใหม่ -> แสดงบทนำ (Intro)
            GameSession.isMainStoryEvent -> {
                GameSession.isMainStoryEvent = false
                ShopLibrary.resetChapterShop()
                StoryLibrary.getChapterStart(GameSession.currentChapter)
            }
            // 4. ร้านค้าสุ่มเกิดช่วงกลางบท
            !ShopLibrary.shopEventSpawned && (GameSession.encounterCount % 8 >= 4) -> {
                ShopLibrary.shopEventSpawned = true
                StoryLibrary.getShopStoryEvent()
            }
            // 5. เหตุการณ์สุ่มหลากหลาย (ทหาร 70% / ทั่วไป 30%)
            else -> StoryLibrary.getRandomEvent()
        }
        
        findViewById<TextView>(R.id.tvStoryTitle).text = event.title
        findViewById<TextView>(R.id.tvStoryDescription).text = event.description
        findViewById<ImageView>(R.id.ivEventImage).setImageResource(event.imageRes)

        val container = findViewById<LinearLayout>(R.id.llOptionsContainer)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val btnContinue = findViewById<Button>(R.id.btnContinue)

        event.options.forEach { option ->
            val button = Button(this).apply {
                text = option.text
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
                // ถ้ามีการสู้รบ ให้ไปหน้าต่อสู้
                startActivity(Intent(this, BattleActivity::class.java))
                finish()
            } else {
                // ถ้าเป็นเนื้อเรื่องต่อเนื่อง (เช่น เลือกชะตากรรมบอสเสร็จแล้วไป Epilogue)
                // ให้ Refresh หน้า StoryActivity เพื่อแสดงเหตุการณ์ถัดไป
                val intent = Intent(this, StoryActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
