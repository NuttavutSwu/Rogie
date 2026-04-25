package com.rogie.threekingdoms.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.game.GameSession
import com.rogie.threekingdoms.meta.CharacterId
import com.rogie.threekingdoms.meta.CharacterLibrary
import com.rogie.threekingdoms.meta.MetaProgressionManager
import com.rogie.threekingdoms.meta.TalentTreeLibrary

class CharacterSelectionActivity : AppCompatActivity() {
    private lateinit var unlockedCharacters: List<CharacterId>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_selection)

        unlockedCharacters = MetaProgressionManager.unlockedCharacters(this)
        val spinner = findViewById<Spinner>(R.id.spinnerCharacter)
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            unlockedCharacters.map { CharacterLibrary.get(it).displayName }
        )

        val tvPassive = findViewById<TextView>(R.id.tvPassive)
        val tvSkillPreview = findViewById<TextView>(R.id.tvSkillPreview)
        val tvStats = findViewById<TextView>(R.id.tvCharacterStats)

        fun refreshPreview() {
            val character = CharacterLibrary.get(unlockedCharacters[spinner.selectedItemPosition])
            tvPassive.text = "${character.passiveName}: ${character.passiveDescription}"
            tvStats.text = getString(
                R.string.character_panel_fmt,
                "${character.maxHearts}",
                "${character.attack}",
                "${character.defense}",
                "${character.energy}",
                "${(character.critChance * 100).toInt()}"
            )
            val tree = TalentTreeLibrary.treeFor(character.id).nodes.take(6).joinToString("\n") { "- ${it.name}" }
            tvSkillPreview.text = tree
        }

        spinner.setOnItemSelectedListener(
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) = refreshPreview()
                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) = Unit
            }
        )
        refreshPreview()

        findViewById<Button>(R.id.btnBeginRun).setOnClickListener {
            val chosen = unlockedCharacters[spinner.selectedItemPosition]
            val character = CharacterLibrary.get(chosen)
            val totalUpgrades = MetaProgressionManager.getTotalUpgradeLevels(this)
            val unlockedTalents = MetaProgressionManager.getUnlockedTalents(this, chosen)
            GameSession.startRun(character, totalUpgrades, unlockedTalents)
            startActivity(Intent(this, BattleActivity::class.java))
            finish()
        }
    }
}
