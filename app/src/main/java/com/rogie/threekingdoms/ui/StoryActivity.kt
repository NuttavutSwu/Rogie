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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        val event = when {
            // 1. ถ้าเพิ่งปราบบอส -> เลือก Spare/Execute
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
            // 3. ถ้าเริ่มบทใหม่ -> บทนำ
            GameSession.isMainStoryEvent -> {
                GameSession.isMainStoryEvent = false
                ShopLibrary.resetChapterShop()
                StoryLibrary.getChapterStart(GameSession.currentChapter)
            }
            // 4. ร้านค้าจะโผล่มาช่วงกลางบท (ด่านที่ 4 ของ 8 ด่าน)
            !ShopLibrary.shopEventSpawned && (GameSession.encounterCount % 8 >= 4) -> {
                ShopLibrary.shopEventSpawned = true
                StoryLibrary.getShopStoryEvent()
            }
            // 5. เหตุการณ์สุ่มปกติ
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
                    
                    // อัปเกรดด่านถ้าเลือกชะตากรรมบอสเสร็จแล้ว
                    if (option.isSpare || option.isExecute) {
                        GameSession.currentChapter++
                    }

                    tvResult.text = option.resultText
                    tvResult.visibility = View.VISIBLE
                    container.visibility = View.GONE
                    btnContinue.visibility = View.VISIBLE
                    
                    if (option.triggerFight && option.forcedEnemy != null) {
                        GameSession.setForcedEnemy(option.forcedEnemy)
                    }
                }
            }
            container.addView(button)
        }

        btnContinue.setOnClickListener {
            // เดินทางเข้าสู่หน้าต่อสู้หรือด่านถัดไป
            startActivity(Intent(this, BattleActivity::class.java))
            finish()
        }
    }
}
