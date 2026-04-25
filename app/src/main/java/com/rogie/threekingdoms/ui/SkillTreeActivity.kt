package com.rogie.threekingdoms.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.media.AudioManager
import android.media.ToneGenerator
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.meta.CharacterId
import com.rogie.threekingdoms.meta.CharacterLibrary
import com.rogie.threekingdoms.meta.MetaProgressionManager
import com.rogie.threekingdoms.meta.TalentNode
import com.rogie.threekingdoms.meta.TalentRarity
import com.rogie.threekingdoms.meta.TalentTreeLibrary

class SkillTreeActivity : AppCompatActivity() {
    private lateinit var unlockedCharacters: List<CharacterId>
    private lateinit var currentCharacter: CharacterId
    private val toneGenerator by lazy { ToneGenerator(AudioManager.STREAM_NOTIFICATION, 90) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skill_tree)

        unlockedCharacters = MetaProgressionManager.unlockedCharacters(this)
        val spinner = findViewById<Spinner>(R.id.spinnerSkillCharacter)
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            unlockedCharacters.map { CharacterLibrary.get(it).displayName }
        )

        spinner.setOnItemSelectedListener(
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    currentCharacter = unlockedCharacters[position]
                    renderTree()
                }
                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) = Unit
            }
        )
        currentCharacter = unlockedCharacters.first()
        renderTree()
    }

    private fun renderTree() {
        val character = CharacterLibrary.get(currentCharacter)
        findViewById<TextView>(R.id.tvSkillPoints).text = getString(
            R.string.skill_points_fmt,
            MetaProgressionManager.getSkillPoints(this).toString()
        )
        findViewById<TextView>(R.id.tvTreeLegend).text = getString(
            R.string.tree_legend_fmt,
            character.displayName
        )

        val nodesContainer = findViewById<LinearLayout>(R.id.llTalentNodes)
        nodesContainer.removeAllViews()
        val tree = TalentTreeLibrary.treeFor(currentCharacter)
        tree.nodes.forEach { node ->
            val row = layoutInflater.inflate(R.layout.item_talent_node, nodesContainer, false)
            bindNode(row, node)
            nodesContainer.addView(row)
        }
    }

    private fun bindNode(view: android.view.View, node: TalentNode) {
        val isUnlocked = MetaProgressionManager.hasTalentUnlocked(this, currentCharacter, node.id)
        val unlockedSet = MetaProgressionManager.getUnlockedTalents(this, currentCharacter)
        val requirementsMet = node.requires.all { unlockedSet.contains(it) }

        view.findViewById<TextView>(R.id.tvNodeInfo).text =
            "${node.path.name}: ${node.name}\n${node.description}\nCost: ${node.cost} | Requires: ${
                if (node.requires.isEmpty()) "None" else node.requires.joinToString(", ")
            }"
        view.findViewById<TextView>(R.id.tvNodeState).text =
            if (isUnlocked) "Unlocked" else if (requirementsMet) "Ready" else "Locked"
        val tvRarity = view.findViewById<TextView>(R.id.tvNodeRarity)
        tvRarity.text = "Rarity: ${node.rarity.name}"
        tvRarity.setTextColor(
            ContextCompat.getColor(
                this,
                when (node.rarity) {
                    TalentRarity.COMMON -> R.color.talent_common
                    TalentRarity.RARE -> R.color.talent_rare
                    TalentRarity.LEGENDARY -> R.color.talent_legendary
                }
            )
        )

        view.findViewById<Button>(R.id.btnUnlockNode).apply {
            text = if (isUnlocked) "Unlocked" else getString(R.string.unlock_talent)
            isEnabled = !isUnlocked && requirementsMet
            setOnClickListener {
                val unlocked = MetaProgressionManager.unlockTalent(this@SkillTreeActivity, currentCharacter, node.id)
                if (unlocked) {
                    playUnlockAnimation(view)
                    playUnlockSound(node.rarity)
                    view.postDelayed({ renderTree() }, 220)
                } else {
                    renderTree()
                }
            }
        }
    }

    private fun playUnlockAnimation(view: android.view.View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.05f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.05f, 1f)
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 0.7f, 1f)
        AnimatorSet().apply {
            playTogether(scaleX, scaleY, alpha)
            duration = 280
            start()
        }
    }

    private fun playUnlockSound(rarity: TalentRarity) {
        val toneType = when (rarity) {
            TalentRarity.COMMON -> ToneGenerator.TONE_PROP_BEEP
            TalentRarity.RARE -> ToneGenerator.TONE_PROP_ACK
            TalentRarity.LEGENDARY -> ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD
        }
        toneGenerator.startTone(toneType, 220)
    }

    override fun onDestroy() {
        toneGenerator.release()
        super.onDestroy()
    }
}
