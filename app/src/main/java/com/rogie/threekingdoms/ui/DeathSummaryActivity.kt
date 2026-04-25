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

        val earned = intent.getIntExtra("earned_points", 0)
        val enemies = intent.getIntExtra("enemies_defeated", 0)
        val bosses = intent.getIntExtra("bosses_defeated", 0)
        val chapter = intent.getIntExtra("chapter_reached", 1)
        val totalPoints = MetaProgressionManager.getSkillPoints(this)

        findViewById<TextView>(R.id.tvDeathSummary).text =
            "Run Over\nEnemies defeated: $enemies\nBosses defeated: $bosses\nChapter reached: $chapter"
        findViewById<TextView>(R.id.tvPointsEarned).text =
            "Skill Points earned: $earned\nTotal Skill Points: $totalPoints"

        findViewById<Button>(R.id.btnBackToMenu).setOnClickListener {
            startActivity(Intent(this, MainMenuActivity::class.java))
            finish()
        }
    }
}
