package com.rogie.threekingdoms.meta

import android.content.Context

object MetaProgressionManager {
    private const val PREFS = "three_kingdoms_meta"
    private const val KEY_POINTS = "skill_points"
    private const val KEY_UNLOCKS = "unlocks"

    private val defaultUnlocked = setOf(
        CharacterId.GUAN_YU.name,
        CharacterId.ZHUGE_LIANG.name,
        CharacterId.ZHOU_YU.name
    )

    fun getSkillPoints(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_POINTS, 0)
    }

    fun addSkillPoints(context: Context, amount: Int) {
        if (amount <= 0) return
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val now = prefs.getInt(KEY_POINTS, 0)
        prefs.edit().putInt(KEY_POINTS, now + amount).apply()
    }

    fun unlockedCharacters(context: Context): List<CharacterId> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val raw = prefs.getStringSet(KEY_UNLOCKS, null) ?: defaultUnlocked
        return CharacterId.entries.filter { raw.contains(it.name) }
    }

    fun isUnlocked(context: Context, character: CharacterId): Boolean {
        return unlockedCharacters(context).contains(character)
    }

    fun unlockCharacter(context: Context, character: CharacterId): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val now = (prefs.getStringSet(KEY_UNLOCKS, null) ?: defaultUnlocked).toMutableSet()
        val added = now.add(character.name)
        if (added) {
            prefs.edit().putStringSet(KEY_UNLOCKS, now).apply()
        }
        return added
    }

    fun hasTalentUnlocked(context: Context, character: CharacterId, nodeId: String): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getBoolean(talentKey(character, nodeId), false)
    }

    fun getUnlockedTalents(context: Context, character: CharacterId): Set<String> {
        val tree = TalentTreeLibrary.treeFor(character)
        return tree.nodes.map { it.id }.filter { hasTalentUnlocked(context, character, it) }.toSet()
    }

    fun getTotalUpgradeLevels(context: Context): Int {
        var total = 0
        CharacterId.entries.forEach { character ->
            total += getUnlockedTalents(context, character).size
        }
        return total
    }

    fun unlockTalent(context: Context, character: CharacterId, nodeId: String): Boolean {
        val tree = TalentTreeLibrary.treeFor(character)
        val node = tree.nodes.firstOrNull { it.id == nodeId } ?: return false
        if (hasTalentUnlocked(context, character, nodeId)) return false

        val unlocked = getUnlockedTalents(context, character)
        if (!node.requires.all { unlocked.contains(it) }) return false

        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val points = prefs.getInt(KEY_POINTS, 0)
        if (points < node.cost) return false

        prefs.edit()
            .putInt(KEY_POINTS, points - node.cost)
            .putBoolean(talentKey(character, nodeId), true)
            .apply()
        return true
    }

    @Deprecated("Use hasTalentUnlocked")
    fun getSkillLevel(context: Context, character: CharacterId, nodeId: String): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return if (prefs.getBoolean(talentKey(character, nodeId), false)) 1 else 0
    }

    private fun talentKey(character: CharacterId, nodeId: String): String {
        return "talent_${character.name}_$nodeId"
    }
}
