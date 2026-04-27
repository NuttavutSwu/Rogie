package com.rogie.threekingdoms.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.game.GameSession
import com.rogie.threekingdoms.model.StoryLibrary

class StoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        val event = StoryLibrary.getRandomEvent()
        
        findViewById<TextView>(R.id.tvStoryTitle).text = event.title
        findViewById<TextView>(R.id.tvStoryDescription).text = event.description

        val container = findViewById<LinearLayout>(R.id.llOptionsContainer)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val btnContinue = findViewById<Button>(R.id.btnContinue)

        event.options.forEach { option ->
            val button = Button(this).apply {
                text = option.text
                setOnClickListener {
                    option.onChoice(GameSession.player)
                    tvResult.text = option.resultText
                    tvResult.visibility = View.VISIBLE
                    container.visibility = View.GONE
                    btnContinue.visibility = View.VISIBLE
                }
            }
            container.addView(button)
        }

        btnContinue.setOnClickListener {
            startActivity(Intent(this, BattleActivity::class.java))
            finish()
        }
    }
}
