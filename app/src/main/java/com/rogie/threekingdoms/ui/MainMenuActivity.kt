package com.rogie.threekingdoms.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.meta.MetaProgressionManager

class MainMenuActivity : AppCompatActivity() {
    private lateinit var tvMetaPoints: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        tvMetaPoints = findViewById(R.id.tvMetaPoints)
        refreshPoints()

        findViewById<Button>(R.id.btnStartGame).setOnClickListener {
            startActivity(Intent(this, CharacterSelectionActivity::class.java))
        }

        findViewById<Button>(R.id.btnSkillTree).setOnClickListener {
            startActivity(Intent(this, SkillTreeActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshPoints()
    }

    private fun refreshPoints() {
        val points = MetaProgressionManager.getSkillPoints(this)
        tvMetaPoints.text = getString(R.string.skill_points_fmt, points.toString())
    }
}
