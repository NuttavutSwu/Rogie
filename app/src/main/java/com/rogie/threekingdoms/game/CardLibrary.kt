package com.rogie.threekingdoms.game

import com.rogie.threekingdoms.model.Card
import com.rogie.threekingdoms.model.CardType
import com.rogie.threekingdoms.model.CardRarity
import com.rogie.threekingdoms.model.EffectType
import com.rogie.threekingdoms.meta.CharacterId

object CardLibrary {
    fun starterDeck(character: CharacterId): List<Card> {
        return when (character) {
            CharacterId.LU_BU -> listOf(slash(), slash(), battleFever(), crushingHalberd(), ironBody())
            CharacterId.CAO_CAO -> listOf(slash(), scheme(), duplicate(), suppress(), decree())
            CharacterId.ZHUGE_LIANG -> listOf(foresight(), trap(), trigger(), windCall(), fireStrategy())
            CharacterId.GUAN_YU -> listOf(slash(), block(), heavyStrike(), honorGuard(), dragonBlade())
            CharacterId.ZHOU_YU -> listOf(fireSlash(), windFan(), chainLink(), ember(), crimsonInferno())
        }
    }

    // --- Base Cards ---
    private fun slash() = Card("lb_slash", "Slash", 1, "Deal 1 damage.", CardType.ATTACK, EffectType.DAMAGE, 1, CardRarity.COMMON, listOf("[Slash]"))
    private fun block() = Card("common_block", "Iron Guard", 1, "Gain 1 Block.", CardType.DEFENSE, EffectType.BLOCK, 1, CardRarity.COMMON, listOf("[Defense]"))

    // --- Lu Bu: Rage & Risk (Full Set) ---
    fun battleFever() = Card("lb_fever", "Battle Fever", 1, "Gain +1 Rage each time you play a card.", CardType.SKILL, EffectType.RAGE_BOOST, 1, CardRarity.RARE, listOf("[Rage]", "[Buff]"))
    fun overdriveRage() = Card("lb_overdrive", "Overdrive Rage", 2, "Double your Rage; lose 5 HP at end of turn.", CardType.SKILL, EffectType.RAGE_BOOST, 2, CardRarity.EPIC, listOf("[Rage]", "[Risk]"))
    fun crushingHalberd() = Card("lb_halberd", "Crushing Halberd", 2, "Deal 2 damage. If Rage >= 3, Stun target.", CardType.ATTACK, EffectType.DAMAGE, 2, CardRarity.RARE, listOf("[Slash]", "[Rage]"))
    fun fallingSky() = Card("lb_falling", "Falling Sky Strike", 3, "Deal 3 damage to all; lose 4 HP.", CardType.ATTACK, EffectType.DAMAGE, 3, CardRarity.EPIC, listOf("[AoE]", "[Risk]"))
    fun executionSlash() = Card("lb_execute", "Execution Slash", 2, "If target HP < 40%, instantly kill.", CardType.ATTACK, EffectType.EXECUTE, 0, CardRarity.RARE, listOf("[Execute]"))
    fun lastBreath() = Card("lb_breath", "Last Breath", 0, "If HP < 30%, deal +50% damage.", CardType.SKILL, EffectType.LOW_HP_BUFF, 0, CardRarity.RARE, listOf("[Buff]", "[Risk]"))
    fun bloodExplosion() = Card("lb_explode", "Blood Explosion", 2, "Lose 5 HP -> deal 1 damage twice.", CardType.ATTACK, EffectType.MULTI_HIT, 1, CardRarity.RARE, listOf("[Risk]", "[Combo]"))
    fun painMirror() = Card("lb_mirror", "Pain Mirror", 1, "Reflect 100% of damage taken.", CardType.SKILL, EffectType.REFLECT, 0, CardRarity.RARE, listOf("[Counter]", "[Risk]"))
    fun warRecovery() = Card("lb_recovery", "War Recovery", 1, "Restore HP equal to 2x Rage (consumes Rage).", CardType.SKILL, EffectType.HEAL, 2, CardRarity.RARE, listOf("[Heal]", "[Rage]"))
    fun ironBody() = Card("lb_iron", "Iron Body", 2, "Reduce damage by 50%; cannot attack.", CardType.DEFENSE, EffectType.BLOCK, 2, CardRarity.RARE, listOf("[Defense]"))
    fun chainAssault() = Card("lb_chain", "Chain Assault", 1, "If played after [Slash], repeat.", CardType.ATTACK, EffectType.DAMAGE, 1, CardRarity.RARE, listOf("[Slash]", "[Combo]"))
    fun relentlessFury() = Card("lb_fury", "Relentless Fury", 2, "When kill, play 1 free card.", CardType.SKILL, EffectType.REDUCE_COST, 1, CardRarity.EPIC, listOf("[Combo]", "[Rage]"))
    fun warMomentum() = Card("lb_momentum", "War Momentum", 1, "Next card costs -1 if Rage >= 2.", CardType.SKILL, EffectType.REDUCE_COST, 1, CardRarity.RARE, listOf("[Buff]", "[Combo]"))
    fun demonicInstinct() = Card("lb_instinct", "Demonic Instinct", 2, "If HP < 50%, guaranteed Crits.", CardType.SKILL, EffectType.LOW_HP_BUFF, 1, CardRarity.RARE, listOf("[Buff]", "[Risk]"))
    fun rageIncarnate() = Card("lb_transform", "Rage Incarnate", 3, "Rage replaces Energy for 2 turns.", CardType.TRANSFORMATION, EffectType.TRANSFORM, 2, CardRarity.LEGENDARY, listOf("[Transform]", "[Rage]"))

    // --- Other Characters Placeholder References ---
    private fun heavyStrike() = Card("common_heavy", "Heavy Strike", 2, "Deal 2 damage.", CardType.ATTACK, EffectType.DAMAGE, 2, CardRarity.RARE, listOf("[Heavy]"))
    private fun scheme() = Card("cc_scheme", "Dark Scheme", 2, "Lose 1 energy.", CardType.SKILL, EffectType.STEAL, 1, CardRarity.RARE, listOf("[Control]"))
    private fun duplicate() = Card("cc_duplicate", "Mirror Image", 2, "Copy last card.", CardType.SPECIAL, EffectType.DRAW_CARDS, 1, CardRarity.EPIC, listOf("[Control]"))
    private fun suppress() = Card("cc_suppress", "Suppress", 1, "Deal 1 dmg, strength -1.", CardType.ATTACK, EffectType.DAMAGE, 1, CardRarity.COMMON, listOf("[Control]"))
    private fun decree() = Card("cc_decree", "Imperial Decree", 3, "Steal Strength.", CardType.SPECIAL, EffectType.STEAL, 1, CardRarity.LEGENDARY, listOf("[Control]"))
    private fun foresight() = Card("zl_foresight", "Foresight", 1, "Draw 2.", CardType.SKILL, EffectType.DRAW_CARDS, 2, CardRarity.RARE, listOf("[Strategy]"))
    private fun trap() = Card("zl_trap", "Eight Trigram Trap", 2, "Set trap.", CardType.SPECIAL, EffectType.BLOCK, 2, CardRarity.RARE, listOf("[Trap]"))
    private fun trigger() = Card("zl_trigger", "Strategic Release", 1, "Trigger traps.", CardType.SPECIAL, EffectType.DAMAGE, 1, CardRarity.RARE, listOf("[Trigger]"))
    private fun windCall() = Card("zl_wind", "Gale Call", 1, "Apply Bleed.", CardType.SKILL, EffectType.BLEED, 1, CardRarity.COMMON, listOf("[Wind]"))
    private fun fireStrategy() = Card("zl_fire", "Fire Attack", 2, "Burn.", CardType.ATTACK, EffectType.BURN_STRIKE, 1, CardRarity.COMMON, listOf("[Fire]"))
    private fun starMap() = Card("zl_stars", "Prayer to Stars", 4, "Ultimate.", CardType.SPECIAL, EffectType.BUFF_STRENGTH, 3, CardRarity.LEGENDARY, listOf("[Strategy]"))
    private fun honorGuard() = Card("gy_stance", "God of War Stance", 2, "Strength +1.", CardType.SKILL, EffectType.STANCE_CHANGE, 1, CardRarity.EPIC, listOf("[Defense]", "[Slash]"))
    private fun dragonBlade() = Card("gy_blade", "Green Dragon Blade", 3, "Deal 3.", CardType.ATTACK, EffectType.DAMAGE, 3, CardRarity.LEGENDARY, listOf("[Slash]"))
    private fun fireSlash() = Card("zy_slash", "Ignited Blade", 1, "Fire.", CardType.ATTACK, EffectType.DAMAGE, 1, CardRarity.COMMON, listOf("[Fire]"))
    private fun windFan() = Card("zy_fan", "Breeze fan", 1, "Wind.", CardType.SKILL, EffectType.DRAW_CARDS, 1, CardRarity.COMMON, listOf("[Wind]"))
    private fun chainLink() = Card("zy_link", "Linked Ships", 2, "Double Burn.", CardType.SKILL, EffectType.BUFF_STRENGTH, 1, CardRarity.RARE, listOf("[Fire]"))
    private fun ember() = Card("zy_ember", "Ember", 0, "No damage.", CardType.ATTACK, EffectType.DAMAGE, 0, CardRarity.COMMON, listOf("[Fire]"))
    private fun crimsonInferno() = Card("zy_inferno", "Crimson Inferno", 3, "Burn.", CardType.SPECIAL, EffectType.BURN_STRIKE, 3, CardRarity.LEGENDARY, listOf("[Fire]", "[Wind]"))

    fun rewardPool(): List<Card> = listOf(
        battleFever(), overdriveRage(), crushingHalberd(), fallingSky(),
        executionSlash(), lastBreath(), bloodExplosion(), painMirror(),
        warRecovery(), ironBody(), chainAssault(), relentlessFury(),
        warMomentum(), demonicInstinct(), rageIncarnate()
    )
}
