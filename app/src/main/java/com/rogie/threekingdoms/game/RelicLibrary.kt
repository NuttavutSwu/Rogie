package com.rogie.threekingdoms.game

import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.model.Relic
import com.rogie.threekingdoms.model.EffectType

object RelicLibrary {
    fun getAllRelics(): List<Relic> = listOf(
        // Common
        Relic("relic_iron_will", "Iron Will", "Gain +2 block at the start of each battle", EffectType.BLOCK, 2, R.drawable.ic_relic_shield),
        Relic("relic_war_coin", "War Coin", "Gain +20 gold after combat", EffectType.STEAL, 20, R.drawable.ic_relic_coin),
        Relic("relic_quick_blade", "Quick Blade", "First attack each combat deals +3 damage", EffectType.DAMAGE, 3, R.drawable.ic_relic_sword),
        Relic("relic_soldier_band", "Soldier's Band", "Heal 3 HP after combat", EffectType.HEAL, 3, R.drawable.ic_relic_band),
        Relic("relic_guard_charm", "Guard Charm", "Gain +1 block every turn", EffectType.BLOCK, 1, R.drawable.ic_relic_charm),
        Relic("relic_blood_token", "Blood Token", "Lose 1 HP -> gain +2 damage this turn", EffectType.BUFF_STRENGTH, 2, R.drawable.ic_relic_token),
        Relic("relic_focus_ring", "Focus Ring", "Draw +1 card on first turn", EffectType.DRAW_CARDS, 1, R.drawable.ic_relic_ring),
        Relic("relic_steel_edge", "Steel Edge", "Attacks deal +2 damage", EffectType.DAMAGE, 2, R.drawable.ic_relic_blade),

        // Rare
        Relic("relic_battle_standard", "Battle Standard", "Gain +3 attack for 2 turns at start", EffectType.BUFF_STRENGTH, 3, R.drawable.ic_relic_banner),
        Relic("relic_shield_core", "Shield Core", "Block does not decay for 1 turn", EffectType.BLOCK, 1, R.drawable.ic_relic_core),
        Relic("relic_tactical_scroll", "Tactical Scroll", "Reduce cost of first skill by 1", EffectType.REDUCE_COST, 1, R.drawable.ic_relic_scroll),
        Relic("relic_chaos_idol", "Chaos Idol", "+5 damage but lose 2 HP per turn", EffectType.BUFF_STRENGTH, 5, R.drawable.ic_relic_idol),
        Relic("relic_honor_emblem", "Honor Emblem", "Gain +1 Honor each turn", EffectType.HONOR_BOOST, 1, R.drawable.ic_relic_emblem),
        Relic("relic_twin_fang", "Twin Fang", "Every 3rd attack hits twice", EffectType.MULTI_HIT, 3, R.drawable.ic_relic_fangs),
        Relic("relic_wind_cloak", "Wind Cloak", "20% chance to evade attacks", EffectType.BLOCK, 20, R.drawable.ic_relic_cloak),

        // Epic
        Relic("relic_war_engine", "War Engine", "Gain +2 energy on turn 1", EffectType.BUFF_STRENGTH, 2, R.drawable.ic_relic_engine),
        Relic("relic_blood_pact", "Blood Pact", "Damage +50% when HP < 50%", EffectType.BUFF_STRENGTH, 50, R.drawable.ic_relic_pact),
        Relic("relic_divine_guard", "Divine Guard", "Negate first damage each combat", EffectType.IMMUNITY, 1, R.drawable.ic_relic_divine),
        Relic("relic_storm_core", "Storm Core", "Every 5 cards played -> 10 AoE damage", EffectType.DAMAGE, 10, R.drawable.ic_relic_storm),
        Relic("relic_ancient_coin", "Ancient Coin", "Double gold gain", EffectType.STEAL, 2, R.drawable.ic_relic_ancient_coin),
        Relic("relic_oath_blade", "Oath Blade", "Damage based on Honor", EffectType.HONOR_BOOST, 1, R.drawable.ic_relic_oath),

        // Legendary
        Relic("relic_crown_war", "Crown of War", "+2 energy every turn", EffectType.BUFF_STRENGTH, 2, R.drawable.ic_relic_crown),
        Relic("relic_dragon_heart", "Dragon Heart", "Revive once with 50% HP", EffectType.HEAL, 50, R.drawable.ic_relic_heart),
        Relic("relic_chaos_core", "Chaos Core", "Double damage but take double damage", EffectType.BUFF_STRENGTH, 100, R.drawable.ic_relic_chaos_core),
        Relic("relic_eternal_banner", "Eternal Banner", "All attacks gain +5 attack", EffectType.BUFF_STRENGTH, 5, R.drawable.ic_relic_eternal),
        Relic("relic_fate_breaker", "Fate Breaker", "Every 10th attack deals 100 damage", EffectType.DAMAGE, 100, R.drawable.ic_relic_fate)
    )

    fun getRandomRelic(): Relic = getAllRelics().random()
    
    fun getPoolByRarity(rarity: String): List<Relic> {
        // Simple implementation for now
        return getAllRelics().shuffled()
    }
}
