package com.rogie.threekingdoms.game

import com.rogie.threekingdoms.model.Card
import com.rogie.threekingdoms.model.CardType
import com.rogie.threekingdoms.model.EffectType
import com.rogie.threekingdoms.model.Enemy
import com.rogie.threekingdoms.model.Player
import com.rogie.threekingdoms.meta.CharacterId
import kotlin.math.max
import kotlin.random.Random

class BattleManager(
    private val player: Player,
    private val enemy: Enemy,
    private val deckManager: DeckManager
) {
    private var usedDiscountThisTurn = false
    private var usedFirstAttackBonusThisTurn = false

    fun startBattle() {
        player.energy = player.baseEnergy
        player.block = 0
        player.strength = 0
        usedDiscountThisTurn = false
        usedFirstAttackBonusThisTurn = false
        if (GameSession.selectedCharacter == CharacterId.LU_BU && GameSession.luBuSelfHeartLossStart) {
            player.hp = max(1, player.hp - 20)
        }
        player.block += GameSession.extraStartBlock
        deckManager.drawCards(5 + GameSession.drawBonusPerTurn)
    }

    fun playerPlayCard(card: Card): String {
        val canDiscount = GameSession.skillDiscountEnabled &&
            (card.type == CardType.SKILL || card.type == CardType.STRATEGY)
        val effectiveCost = if (canDiscount && (!GameSession.discountOncePerTurn || !usedDiscountThisTurn)) {
            usedDiscountThisTurn = true
            max(0, card.energyCost - 1)
        } else card.energyCost
        if (effectiveCost > player.energy) return "Not enough energy."
        player.energy -= effectiveCost

        val log = when (card.effect) {
            EffectType.DAMAGE -> {
                val dealt = dealDamageToEnemy(card.value + player.strength)
                "${card.name} dealt $dealt damage."
            }
            EffectType.BLOCK -> {
                val boost = if (card.type == CardType.STRATEGY) GameSession.strategyMultiplier else 1.0
                player.block += (card.value * boost).toInt()
                if (card.name.contains("Zhuge Liang")) player.strength += 1
                "${card.name} raised your defenses."
            }
            EffectType.BURN_STRIKE -> {
                val dealt = dealDamageToEnemy(card.value + player.strength)
                enemy.burn += if (GameSession.selectedCharacter == CharacterId.ZHOU_YU) 5 else 3
                "$dealt damage and 3 burn applied."
            }
            EffectType.BUFF_STRENGTH -> {
                player.strength += card.value
                "Strength increased by ${card.value}."
            }
            EffectType.DRAIN -> {
                val dealt = dealDamageToEnemy(card.value + player.strength)
                player.hp = minOf(player.maxHp, player.hp + 4 + GameSession.drainBonusHeal)
                "Drained life for $dealt damage."
            }
            EffectType.MULTI_HIT -> {
                var total = 0
                repeat(3) { total += dealDamageToEnemy(card.value + player.strength) }
                "Delivered three strikes for $total total damage."
            }
        }
        deckManager.removeFromHand(card)
        return log
    }

    fun endPlayerTurn(): String {
        if (enemy.burn > 0) {
            enemy.hp -= enemy.burn
            enemy.burn = max(0, enemy.burn - 1)
        }
        if (enemy.hp <= 0) return "Enemy defeated."

        val incomingRaw = max(0, enemy.damage - player.block)
        val incoming = (incomingRaw * (1.0 - GameSession.damageReduction)).toInt()
        player.hp -= incoming
        player.block = 0
        deckManager.discardHand()
        player.energy = player.baseEnergy
        usedDiscountThisTurn = false
        usedFirstAttackBonusThisTurn = false
        deckManager.drawCards(5 + GameSession.drawBonusPerTurn)
        return "${enemy.name} attacked for $incoming damage."
    }

    private fun dealDamageToEnemy(rawDamage: Int): Int {
        var damageMultiplier = GameSession.attackMultiplier
        val hpRatio = player.hp.toDouble() / max(1, player.maxHp).toDouble()
        if (GameSession.selectedCharacter == CharacterId.LU_BU) {
            if (hpRatio <= 0.5) damageMultiplier += 0.50
            if (hpRatio <= 0.3) damageMultiplier += 1.00
        }
        if (!usedFirstAttackBonusThisTurn) {
            damageMultiplier += GameSession.firstAttackBonusPerTurn
            usedFirstAttackBonusThisTurn = true
        }
        val crit = if (Random.nextDouble() < GameSession.critChance) GameSession.critDamageMultiplier else 1.0
        val scaledDamage = (rawDamage * damageMultiplier * crit).toInt()
        val actual = max(0, scaledDamage - enemy.block)
        enemy.hp -= actual
        enemy.block = max(0, enemy.block - scaledDamage)
        if (enemy.hp <= 0 && GameSession.energyOnKill > 0) {
            player.energy = minOf(player.baseEnergy + 2, player.energy + GameSession.energyOnKill)
        }
        return actual
    }
}
