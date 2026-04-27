package com.rogie.threekingdoms.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.meta.MetaProgressionManager

class DeathSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_death_summary)

        MetaProgressionManager.incrementDeathCount(this)
        val deathCount = MetaProgressionManager.getDeathCount(this)

        val tvMessage = findViewById<TextView>(R.id.tvDeathSummary)
        tvMessage.text = when {
            deathCount == 1 -> "การเดินทางครั้งแรกสิ้นสุดลง... แต่นี่เป็นเพียงจุดเริ่มต้นของกงล้อแห่งกรรม"
            deathCount < 5 -> "ความตายครั้งที่ $deathCount... วิญญาณของท่านเริ่มแปดเปื้อนด้วยไอแห่งสมรภูมิ"
            else -> "ยินดีที่ได้พบกันอีกครั้ง... ท่านแม่ทัพ ท่านยังไม่เหนื่อยกับการรบที่ไม่มีวันสิ้นสุดนี้อีกหรือ?"
        }

        findViewById<Button>(R.id.btnBackToMenu).setOnClickListener {
            startActivity(Intent(this, MainMenuActivity::class.java))
            finish()
        }
    }
}
