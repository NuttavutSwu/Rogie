package com.rogie.threekingdoms.game

import com.rogie.threekingdoms.model.EffectType

data class Ability(
    val id: String,
    val name: String,
    val description: String,
    val effect: EffectType,
    val value: Int,
    val tag: String,
    val rarity: String
)

object AbilityLibrary {
    fun getAllAbilities(): List<Ability> = listOf(
        // Common
        Ability("ab_strike_boost", "Strike Boost", "Attacks deal +2 damage", EffectType.DAMAGE, 2, "Offense", "Common"),
        Ability("ab_quick_reflex", "Quick Reflex", "Gain 2 block when attacked", EffectType.BLOCK, 2, "Defense", "Common"),
        Ability("ab_minor_heal", "Minor Heal", "Heal 2 HP each turn", EffectType.HEAL, 2, "Utility", "Common"),
        Ability("ab_energy_flow", "Energy Flow", "Gain +1 energy every 3 turns", EffectType.BUFF_STRENGTH, 1, "Utility", "Common"),
        Ability("ab_sharp_mind", "Sharp Mind", "Draw 1 extra card every 2 turns", EffectType.DRAW_CARDS, 1, "Utility", "Common"),

        // Rare
        Ability("ab_counter_stance", "Counter Stance", "Counter attack deals +50% damage", EffectType.BUFF_STRENGTH, 50, "Counter", "Rare"),
        Ability("ab_combo_drive", "Combo Drive", "Every 3 cards -> gain +2 damage", EffectType.BUFF_STRENGTH, 2, "Combo", "Rare"),
        Ability("ab_guard_mastery", "Guard Mastery", "Block effectiveness +25%", EffectType.BLOCK, 25, "Defense", "Rare"),
        Ability("ab_chaos_surge", "Chaos Surge", "Gain damage based on chaosLevel", EffectType.BUFF_STRENGTH, 1, "Chaos", "Rare"),
        Ability("ab_honor_boost", "Honor Boost", "Gain bonus when Honor >= 2", EffectType.HONOR_BOOST, 1, "Honor", "Rare"),

        // Epic
        Ability("ab_infinite_flow", "Infinite Flow", "First card each turn costs 0", EffectType.REDUCE_COST, 1, "Utility", "Epic"),
        Ability("ab_battle_frenzy", "Battle Frenzy", "Gain +2 attack per turn", EffectType.BUFF_STRENGTH, 2, "Offense", "Epic"),
        Ability("ab_perfect_guard", "Perfect Guard", "Negate damage once per turn", EffectType.IMMUNITY, 1, "Defense", "Epic"),
        Ability("ab_shadow_step", "Shadow Step", "30% chance to dodge attacks", EffectType.BLOCK, 30, "Defense", "Epic"),
        Ability("ab_execute_mode", "Execution Mode", "Deal double damage to enemies below 30% HP", EffectType.EXECUTE, 1, "Offense", "Epic"),

        // Legendary
        Ability("ab_time_break", "Time Break", "Take an extra turn every 5 turns", EffectType.BUFF_STRENGTH, 1, "Combo", "Legendary"),
        Ability("ab_god_of_war", "God of War", "All attacks deal double damage", EffectType.BUFF_STRENGTH, 100, "Offense", "Legendary"),
        Ability("ab_absolute_def", "Absolute Defense", "Cannot take more than 10 damage per hit", EffectType.BLOCK, 10, "Defense", "Legendary"),
        Ability("ab_endless_combo", "Endless Combo", "Combo never resets", EffectType.BUFF_STRENGTH, 1, "Combo", "Legendary"),
        Ability("ab_fate_control", "Fate Control", "Reroll card draw once per turn", EffectType.DRAW_CARDS, 1, "Utility", "Legendary")
    )
}
