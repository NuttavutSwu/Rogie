package com.rogie.threekingdoms.game

import com.rogie.threekingdoms.model.Card
import com.rogie.threekingdoms.model.CardType
import com.rogie.threekingdoms.model.CardRarity
import com.rogie.threekingdoms.model.EffectType
import com.rogie.threekingdoms.meta.CharacterId

object CardLibrary {
    fun starterDeck(character: CharacterId): List<Card> {
        return when (character) {
            CharacterId.ZHUGE_LIANG -> listOf(featherFanStrike(), tacticalPlanning(), windBarrier(), predictMovement(), hiddenTrap(), calmStrategy(), swiftCalculation(), focusedStrike())
            CharacterId.GUAN_YU -> listOf(greenDragonSlash(), guardStance(), warDiscipline(), righteousStrike(), honorShield(), calmBeforeStrike(), loyalResolve(), judgmentCut())
            CharacterId.LU_BU -> listOf(slash(), slash(), battleFever(), crushingHalberd(), ironBody())
            CharacterId.CAO_CAO -> listOf(slash(), scheme(), duplicate(), suppress(), decree())
            CharacterId.ZHOU_YU -> listOf(fireSlash(), windFan(), chainLink(), ember(), crimsonInferno())
        }
    }

    // --- Zhuge Liang: Starting Cards ---
    fun featherFanStrike() = Card("zl_fan", "Feather Fan Strike", 1, "Deal 1 damage; gain Insight +1.", CardType.ATTACK, EffectType.DAMAGE, 1, CardRarity.COMMON, listOf("[Attack]", "[Insight]"))
    fun tacticalPlanning() = Card("zl_plan", "Tactical Planning", 1, "Gain Insight +2.", CardType.SKILL, EffectType.INSIGHT_BOOST, 2, CardRarity.COMMON, listOf("[Insight]"))
    fun windBarrier() = Card("zl_wind_b", "Wind Barrier", 1, "Gain block; if Insight >= 2, extra block.", CardType.DEFENSE, EffectType.BLOCK, 1, CardRarity.COMMON, listOf("[Defense]", "[Insight]"))
    fun predictMovement() = Card("zl_predict", "Predict Movement", 0, "Next enemy attack deals -50% damage.", CardType.SKILL, EffectType.STUN, 0, CardRarity.COMMON, listOf("[Control]"))
    fun hiddenTrap() = Card("zl_trap_h", "Hidden Trap", 1, "Set a trap: when enemy attacks, deal 2 damage.", CardType.SKILL, EffectType.TRAP, 2, CardRarity.COMMON, listOf("[Trap]"))
    fun calmStrategy() = Card("zl_calm", "Calm Strategy", 1, "Draw 1; gain Insight +1.", CardType.SKILL, EffectType.DRAW_CARDS, 1, CardRarity.COMMON, listOf("[Insight]", "[Buff]"))
    fun swiftCalculation() = Card("zl_swift", "Swift Calculation", 0, "Reduce next card cost by 1.", CardType.SKILL, EffectType.REDUCE_COST, 1, CardRarity.COMMON, listOf("[Buff]"))
    fun focusedStrike() = Card("zl_focused", "Focused Strike", 2, "Deal 2 damage; +1 if Insight >= 2.", CardType.ATTACK, EffectType.DAMAGE, 2, CardRarity.COMMON, listOf("[Attack]", "[Insight]"))

    // --- Zhuge Liang: Unlockable Cards ---
    fun fireTrapFormation() = Card("zl_u_fire", "Fire Trap Formation", 2, "Set trap: when triggered, deal 2 AoE damage.", CardType.SKILL, EffectType.TRAP, 2, CardRarity.RARE, listOf("[Trap]", "[AoE]"))
    fun chainStrategy() = Card("zl_u_chain", "Chain Strategy", 1, "Trigger all traps instantly.", CardType.SKILL, EffectType.TRAP, 0, CardRarity.RARE, listOf("[Combo]", "[Trap]"))
    fun insightOverload() = Card("zl_u_overload", "Insight Overload", 1, "Consume all Insight; next card gains power.", CardType.SKILL, EffectType.BUFF_STRENGTH, 1, CardRarity.RARE, listOf("[Insight]", "[Burst]"))
    fun eightFormationField() = Card("zl_u_eight", "Eight Formation Field", 2, "Reduce all enemy damage by 50%.", CardType.DEFENSE, EffectType.BLOCK, 2, CardRarity.RARE, listOf("[Control]", "[Defense]"))
    fun windControl() = Card("zl_u_wind", "Wind Control", 1, "Next attack hits all enemies.", CardType.SKILL, EffectType.BUFF_STRENGTH, 1, CardRarity.RARE, listOf("[AoE]", "[Buff]"))
    fun delayedExecution() = Card("zl_u_delay", "Delayed Execution", 2, "Mark enemy; after 1 turn, deal 3 damage.", CardType.SKILL, EffectType.TRAP, 3, CardRarity.RARE, listOf("[Trap]"))
    fun masterPrediction() = Card("zl_u_master", "Master Prediction", 1, "Negate next enemy action.", CardType.SKILL, EffectType.STUN, 1, CardRarity.RARE, listOf("[Control]"))
    fun strategicRecovery() = Card("zl_u_recovery", "Strategic Recovery", 1, "Heal 1 HP; gain Insight +1.", CardType.SKILL, EffectType.HEAL, 1, CardRarity.RARE, listOf("[Heal]", "[Insight]"))
    fun infiniteScheme() = Card("zl_u_infinite", "Infinite Scheme", 2, "All Skill cards cost 0 this turn.", CardType.SKILL, EffectType.REDUCE_COST, 9, CardRarity.EPIC, listOf("[Combo]", "[Buff]"))
    fun trapMastery() = Card("zl_u_mastery", "Trap Mastery", 1, "All traps deal +1 damage.", CardType.SKILL, EffectType.BUFF_STRENGTH, 1, CardRarity.RARE, listOf("[Trap]"))
    fun heavenlyStrategy() = Card("zl_u_heaven", "Heavenly Strategy", 3, "Gain Insight +5; Trigger all traps.", CardType.ULTIMATE, EffectType.DAMAGE, 2, CardRarity.LEGENDARY, listOf("[Ultimate]", "[Trap]", "[Insight]"))

    // --- Guan Yu: Starting Cards (Nerfed) ---
    fun greenDragonSlash() = Card("gy_slash_1", "Green Dragon Slash", 1, "Deal 1 damage; if Honor >= 2, +1 damage.", CardType.ATTACK, EffectType.DAMAGE, 1, CardRarity.COMMON, listOf("[Slash]", "[Honor]"))
    fun guardStance() = Card("gy_guard", "Guard Stance", 1, "Gain 1 block; next time attacked, counter 1 damage.", CardType.DEFENSE, EffectType.BLOCK, 1, CardRarity.COMMON, listOf("[Defense]", "[Counter]"))
    fun warDiscipline() = Card("gy_disc", "War Discipline", 1, "Gain Honor +1.", CardType.SKILL, EffectType.HONOR_BOOST, 1, CardRarity.COMMON, listOf("[Honor]"))
    fun righteousStrike() = Card("gy_right", "Righteous Strike", 2, "Deal 1 damage; double if attacked last turn.", CardType.ATTACK, EffectType.DAMAGE, 1, CardRarity.COMMON, listOf("[Counter]"))
    fun honorShield() = Card("gy_shield", "Honor Shield", 1, "Gain block equal to Honor.", CardType.SKILL, EffectType.BLOCK, 0, CardRarity.COMMON, listOf("[Defense]", "[Honor]"))
    fun calmBeforeStrike() = Card("gy_calm", "Calm Before Strike", 0, "Next attack deals +1 damage.", CardType.SKILL, EffectType.BUFF_STRENGTH, 1, CardRarity.COMMON, listOf("[Buff]"))
    fun loyalResolve() = Card("gy_loyal", "Loyal Resolve", 1, "Heal 1 HP; if Honor >= 2, draw 1.", CardType.SKILL, EffectType.HEAL, 1, CardRarity.COMMON, listOf("[Heal]", "[Honor]"))
    fun judgmentCut() = Card("gy_judge", "Judgment Cut", 2, "Deal 1 damage; ignore defense.", CardType.ATTACK, EffectType.EXECUTE, 1, CardRarity.COMMON, listOf("[Execute]"))

    // --- Guan Yu: Unlockable Cards ---
    fun counterFlow() = Card("gy_u_counter", "Counter Flow", 1, "All counters deal +1 damage this turn.", CardType.SKILL, EffectType.BUFF_STRENGTH, 1, CardRarity.RARE, listOf("[Counter]", "[Combo]"))
    fun bladeOfOath() = Card("gy_u_oath", "Blade of Oath", 2, "Deal 1 damage per 2 Honor stack.", CardType.ATTACK, EffectType.DAMAGE, 1, CardRarity.RARE, listOf("[Honor]", "[Combo]"))
    fun retaliationChain() = Card("gy_u_chain", "Retaliation Chain", 2, "If counter triggered, attack again for 1.", CardType.ATTACK, EffectType.DAMAGE, 1, CardRarity.RARE, listOf("[Counter]", "[Combo]"))
    fun unyieldingWall() = Card("gy_u_wall", "Unyielding Wall", 2, "All damage reduced to 1 this turn.", CardType.DEFENSE, EffectType.BLOCK, 1, CardRarity.RARE, listOf("[Defense]"))
    fun oathbreakerPunish() = Card("gy_u_punish", "Oathbreaker Punish", 2, "Deal 2 damage if enemy used buff.", CardType.ATTACK, EffectType.DAMAGE, 2, CardRarity.RARE, listOf("[Counter]"))
    fun perfectGuard() = Card("gy_u_perfect", "Perfect Guard", 1, "Negate next damage and counter 1.", CardType.DEFENSE, EffectType.COUNTER, 1, CardRarity.RARE, listOf("[Defense]", "[Counter]"))
    fun honorSurge() = Card("gy_u_surge", "Honor Surge", 1, "Gain Honor +2.", CardType.SKILL, EffectType.HONOR_BOOST, 2, CardRarity.RARE, listOf("[Honor]"))

    // --- Lu Bu: Full Set ---
    private fun slash() = Card("lb_slash", "Slash", 1, "Deal 1 damage.", CardType.ATTACK, EffectType.DAMAGE, 1, CardRarity.COMMON, listOf("[Slash]"))
    fun battleFever() = Card("lb_fever", "Battle Fever", 1, "Gain +1 Rage each time you play a card.", CardType.SKILL, EffectType.RAGE_BOOST, 1, CardRarity.RARE, listOf("[Rage]", "[Buff]"))
    fun overdriveRage() = Card("lb_overdrive", "Overdrive Rage", 2, "Double your Rage; lose 1 HP.", CardType.SKILL, EffectType.RAGE_BOOST, 1, CardRarity.EPIC, listOf("[Rage]", "[Risk]"))
    fun crushingHalberd() = Card("lb_halberd", "Crushing Halberd", 2, "Deal 1 damage. If Rage >= 2, Stun target.", CardType.ATTACK, EffectType.DAMAGE, 1, CardRarity.RARE, listOf("[Slash]", "[Rage]"))
    fun fallingSky() = Card("lb_falling", "Falling Sky Strike", 3, "Deal 2 damage to all; lose 1 HP.", CardType.ATTACK, EffectType.DAMAGE, 2, CardRarity.EPIC, listOf("[AoE]", "[Risk]"))
    fun executionSlash() = Card("lb_execute", "Execution Slash", 2, "If target HP < 30%, instantly kill.", CardType.ATTACK, EffectType.EXECUTE, 0, CardRarity.RARE, listOf("[Execute]"))
    fun lastBreath() = Card("lb_breath", "Last Breath", 0, "If HP < 2, deal +1 damage.", CardType.SKILL, EffectType.LOW_HP_BUFF, 1, CardRarity.RARE, listOf("[Buff]", "[Risk]"))
    fun bloodExplosion() = Card("lb_explode", "Blood Explosion", 2, "Lose 1 HP -> deal 1 damage twice.", CardType.ATTACK, EffectType.MULTI_HIT, 1, CardRarity.RARE, listOf("[Risk]", "[Combo]"))
    fun painMirror() = Card("lb_mirror", "Pain Mirror", 1, "Reflect 1 damage taken.", CardType.SKILL, EffectType.REFLECT, 1, CardRarity.RARE, listOf("[Counter]", "[Risk]"))
    fun warRecovery() = Card("lb_recovery", "War Recovery", 1, "Restore 1 HP (consumes 3 Rage).", CardType.SKILL, EffectType.HEAL, 1, CardRarity.RARE, listOf("[Heal]", "[Rage]"))
    fun ironBody() = Card("lb_iron", "Iron Body", 2, "Block 1 damage.", CardType.DEFENSE, EffectType.BLOCK, 1, CardRarity.RARE, listOf("[Defense]"))
    fun chainAssault() = Card("lb_chain", "Chain Assault", 1, "If played after [Slash], repeat for 1.", CardType.ATTACK, EffectType.DAMAGE, 1, CardRarity.RARE, listOf("[Slash]", "[Combo]"))
    fun relentlessFury() = Card("lb_fury", "Relentless Fury", 2, "When kill, play 1 free card.", CardType.SKILL, EffectType.REDUCE_COST, 1, CardRarity.EPIC, listOf("[Combo]", "[Rage]"))
    fun warMomentum() = Card("lb_momentum", "War Momentum", 1, "Next card costs -1 if Rage >= 2.", CardType.SKILL, EffectType.REDUCE_COST, 1, CardRarity.RARE, listOf("[Buff]", "[Combo]"))
    fun demonicInstinct() = Card("lb_instinct", "Demonic Instinct", 2, "Critical chance buff.", CardType.SKILL, EffectType.LOW_HP_BUFF, 1, CardRarity.RARE, listOf("[Buff]", "[Risk]"))
    fun rageIncarnate() = Card("lb_transform", "Rage Incarnate", 3, "Rage replaces Energy.", CardType.TRANSFORMATION, EffectType.TRANSFORM, 1, CardRarity.LEGENDARY, listOf("[Transform]", "[Rage]"))

    // --- Other Characters ---
    private fun scheme() = Card("cc_scheme", "Dark Scheme", 2, "Lose 1 energy.", CardType.SKILL, EffectType.STEAL, 1, CardRarity.RARE, listOf("[Control]"))
    private fun duplicate() = Card("cc_duplicate", "Mirror Image", 2, "Copy card.", CardType.SPECIAL, EffectType.DRAW_CARDS, 1, CardRarity.EPIC, listOf("[Control]"))
    private fun suppress() = Card("cc_suppress", "Suppress", 1, "Deal 1 dmg.", CardType.ATTACK, EffectType.DAMAGE, 1, CardRarity.COMMON, listOf("[Control]"))
    private fun decree() = Card("cc_decree", "Imperial Decree", 3, "Steal stat.", CardType.SPECIAL, EffectType.STEAL, 1, CardRarity.LEGENDARY, listOf("[Control]"))
    private fun fireSlash() = Card("zy_slash", "Ignited Blade", 1, "Fire.", CardType.ATTACK, EffectType.DAMAGE, 1, CardRarity.COMMON, listOf("[Fire]"))
    private fun windFan() = Card("zy_fan", "Breeze fan", 1, "Wind.", CardType.SKILL, EffectType.DRAW_CARDS, 1, CardRarity.COMMON, listOf("[Wind]"))
    private fun chainLink() = Card("zy_link", "Linked Ships", 2, "Double Burn.", CardType.SKILL, EffectType.BUFF_STRENGTH, 1, CardRarity.RARE, listOf("[Fire]"))
    private fun ember() = Card("zy_ember", "Ember", 0, "No damage.", CardType.ATTACK, EffectType.DAMAGE, 0, CardRarity.COMMON, listOf("[Fire]"))
    private fun crimsonInferno() = Card("zy_inferno", "Crimson Inferno", 3, "Burn.", CardType.SPECIAL, EffectType.BURN_STRIKE, 3, CardRarity.LEGENDARY, listOf("[Fire]", "[Wind]"))

    fun rewardPool(): List<Card> = listOf(
        battleFever(), overdriveRage(), crushingHalberd(), fallingSky(),
        executionSlash(), lastBreath(), bloodExplosion(), painMirror(),
        warRecovery(), ironBody(), chainAssault(), relentlessFury(),
        warMomentum(), demonicInstinct(), rageIncarnate(),
        counterFlow(), bladeOfOath(), retaliationChain(), unyieldingWall(),
        oathbreakerPunish(), perfectGuard(), honorSurge(),
        fireTrapFormation(), chainStrategy(), insightOverload(), eightFormationField(),
        windControl(), delayedExecution(), masterPrediction(), strategicRecovery()
    )
}
