package com.rogie.threekingdoms.game

import com.rogie.threekingdoms.model.Card
import com.rogie.threekingdoms.model.CardType
import com.rogie.threekingdoms.model.CardRarity
import com.rogie.threekingdoms.model.EffectType
import com.rogie.threekingdoms.meta.CharacterId

object CardLibrary {
    fun starterDeck(character: CharacterId): List<Card> {
        val base = mutableListOf<Card>()
        when (character) {
            CharacterId.GUAN_YU -> base.addAll(
                listOf(guanYuBlade(), slashAttack(), slashAttack(), shieldFormation(), warCry(), zhangFeiRage())
            )
            CharacterId.ZHUGE_LIANG -> base.addAll(
                listOf(zhugeStrategy(), fireAttack(), shieldFormation(), warCry(), slashAttack(), shieldFormation())
            )
            CharacterId.CAO_CAO -> base.addAll(
                listOf(caoCaoControl(), slashAttack(), fireAttack(), warCry(), shieldFormation(), slashAttack())
            )
            CharacterId.ZHOU_YU -> base.addAll(
                listOf(fireAttack(), fireAttack(), zhugeStrategy(), warCry(), shieldFormation(), slashAttack())
            )
            CharacterId.LU_BU -> base.addAll(
                listOf(luBuSkyPierce(), zhangFeiRage(), slashAttack(), slashAttack(), warCry(), fireAttack())
            )
        }
        return base
    }

    fun rewardPool(): List<Card> = listOf(
        zhangFeiRage(), zhugeStrategy(), caoCaoControl(), luBuSkyPierce(),
        slashAttack(), shieldFormation(), fireAttack(), warCry()
    )

    // Nerfed damages for all cards due to lower HP environment
    fun slashAttack() = Card(
        name = "Slash Attack",
        energyCost = 1,
        description = "Deal 1 damage.",
        type = CardType.ATTACK,
        effect = EffectType.DAMAGE,
        value = 1,
        rarity = CardRarity.COMMON
    )

    fun shieldFormation() = Card(
        name = "Shield Formation",
        energyCost = 1,
        description = "Gain 1 block.",
        type = CardType.DEFENSE,
        effect = EffectType.BLOCK,
        value = 1,
        rarity = CardRarity.COMMON
    )

    fun fireAttack() = Card(
        name = "Fire Attack",
        energyCost = 2,
        description = "Deal 1 damage and inflict 1 burn.",
        type = CardType.STRATEGY,
        effect = EffectType.BURN_STRIKE,
        value = 1,
        rarity = CardRarity.COMMON
    )

    fun warCry() = Card(
        name = "War Cry",
        energyCost = 1,
        description = "Gain 1 strength this combat.",
        type = CardType.SKILL,
        effect = EffectType.BUFF_STRENGTH,
        value = 1,
        rarity = CardRarity.COMMON
    )

    fun guanYuBlade() = Card(
        name = "Guan Yu - Green Dragon Slash",
        energyCost = 2,
        description = "Deal 2 damage.",
        type = CardType.ATTACK,
        effect = EffectType.DAMAGE,
        value = 2,
        rarity = CardRarity.RARE
    )

    fun zhangFeiRage() = Card(
        name = "Zhang Fei - Roaring Fury",
        energyCost = 2,
        description = "Hit 3 times for 1 damage each.",
        type = CardType.ATTACK,
        effect = EffectType.MULTI_HIT,
        value = 1,
        rarity = CardRarity.RARE
    )

    fun zhugeStrategy() = Card(
        name = "Zhuge Liang - Eight Trigrams",
        energyCost = 1,
        description = "Gain 1 block and 1 strength.",
        type = CardType.STRATEGY,
        effect = EffectType.BLOCK,
        value = 1,
        rarity = CardRarity.RARE
    )

    fun caoCaoControl() = Card(
        name = "Cao Cao - Ruthless Command",
        energyCost = 2,
        description = "Deal 1 damage and heal 1.",
        type = CardType.SKILL,
        effect = EffectType.DRAIN,
        value = 1,
        rarity = CardRarity.RARE
    )

    fun luBuSkyPierce() = Card(
        name = "Lu Bu - Sky Piercer",
        energyCost = 3,
        description = "Deal 3 damage.",
        type = CardType.ATTACK,
        effect = EffectType.DAMAGE,
        value = 3,
        rarity = CardRarity.EPIC
    )
}
