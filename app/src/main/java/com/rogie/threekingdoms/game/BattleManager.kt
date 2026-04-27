package com.rogie.threekingdoms.game

import com.rogie.threekingdoms.model.Card
import com.rogie.threekingdoms.model.CardType
import com.rogie.threekingdoms.model.EffectType
import com.rogie.threekingdoms.model.Enemy
import com.rogie.threekingdoms.model.Player
import com.rogie.threekingdoms.model.StatusEffect
import com.rogie.threekingdoms.meta.CharacterId
import com.rogie.threekingdoms.meta.Skill
import kotlin.math.max
import kotlin.random.Random

class BattleManager(
    private val player: Player,
    private val enemy: Enemy,
    private val deckManager: DeckManager
) {
    private var usedDiscountThisTurn = false
    private var usedFirstAttackBonusThisTurn = false
    private var isPlayerTurn = true
    private var enemyStrength = 0

    fun startBattle() {
        player.energy = player.baseEnergy
        player.block = 0
        player.strength = 0
        enemyStrength = 0
        player.statuses.clear()
        enemy.statuses.clear()
        
        // Speed check for turn order
        isPlayerTurn = player.speed >= enemy.speed
        
        usedDiscountThisTurn = false
        usedFirstAttackBonusThisTurn = false
        
        if (GameSession.selectedCharacter == CharacterId.LU_BU && GameSession.luBuSelfHeartLossStart) {
            player.hp = max(1, player.hp - 1)
        }
        player.block += GameSession.extraStartBlock
        
        if (isPlayerTurn) {
            deckManager.drawCards(2 + GameSession.drawBonusPerTurn)
        } else {
            // Enemy goes first
            executeEnemyAI()
            deckManager.drawCards(2 + GameSession.drawBonusPerTurn)
            isPlayerTurn = true
        }
    }

    fun useSkill(skill: Skill): String {
        if (player.energy < skill.energyCost) return "Not enough energy for ${skill.name}."
        player.energy -= skill.energyCost
        
        return when (skill.name) {
            "Godly Strike" -> {
                val dealt = dealDamageToEnemy(2 + player.strength)
                "Guan Yu uses ${skill.name}! Dealt $dealt damage."
            }
            "Wind's Call" -> {
                enemy.statuses.add(StatusEffect(EffectType.STUN, 1))
                "Zhuge Liang uses ${skill.name}! Enemy Stunned."
            }
            "Imperial Decree" -> {
                enemy.damage = max(1, enemy.damage - 1)
                "Cao Cao uses ${skill.name}! Enemy Attack reduced."
            }
            "Fanning the Flames" -> {
                enemy.statuses.add(StatusEffect(EffectType.BURN_STRIKE, 2, 1))
                "Zhou Yu uses ${skill.name}! Applied 2 Burn."
            }
            "Crescent Slash" -> {
                dealDamageToEnemy(1 + player.strength)
                player.statuses.add(StatusEffect(EffectType.RAGE_BOOST, 2, 1))
                "Lu Bu uses ${skill.name}! Gained Rage."
            }
            "Dragon's Awakening" -> {
                player.strength += 2
                deckManager.drawCards(2)
                "ULTIMATE: Guan Yu awakens! +2 Strength, drew 2."
            }
            "Empty Fort Strategy" -> {
                player.block += 2
                "ULTIMATE: Zhuge Liang's Strategy! Gained 2 Block."
            }
            "King of Ambition" -> {
                player.statuses.add(StatusEffect(EffectType.RAGE_BOOST, 3, 2))
                "ULTIMATE: Cao Cao's Ambition! Gained high Rage."
            }
            "Red Cliff Blaze" -> {
                dealDamageToEnemy(1)
                enemy.statuses.add(StatusEffect(EffectType.BURN_STRIKE, 3, 1))
                "ULTIMATE: Red Cliff Blaze! Fire everywhere!"
            }
            "Demon of the Battlefield" -> {
                dealDamageToEnemy(4)
                player.hp = max(1, player.hp - 1)
                "ULTIMATE: Lu Bu's Demon form! Massive 4 damage, lost 1 HP."
            }
            else -> "Skill used."
        }
    }

    fun playerPlayCard(card: Card): String {
        if (card.energyCost > player.energy) return "Not enough energy."
        player.energy -= card.energyCost

        val log = when (card.effect) {
            EffectType.DAMAGE -> {
                val bonus = if (GameSession.selectedCharacter == CharacterId.GUAN_YU && card.name.contains("Slash")) 1 else 0
                val dealt = dealDamageToEnemy(card.value + player.strength + bonus)
                "${card.name} dealt $dealt damage."
            }
            EffectType.BLOCK -> {
                player.block += card.value
                "${card.name} raised defenses."
            }
            EffectType.BURN_STRIKE -> {
                val dealt = dealDamageToEnemy(card.value + player.strength)
                val burnVal = if (GameSession.selectedCharacter == CharacterId.ZHOU_YU) 2 else 1
                enemy.statuses.add(StatusEffect(EffectType.BURN_STRIKE, 2, burnVal))
                "$dealt damage and Burn applied."
            }
            EffectType.BUFF_STRENGTH -> {
                player.strength += card.value
                "Strength increased."
            }
            EffectType.DRAIN -> {
                dealDamageToEnemy(card.value + player.strength)
                player.hp = minOf(player.maxHp, player.hp + 1)
                "Drained 1 HP."
            }
            EffectType.MULTI_HIT -> {
                var total = 0
                repeat(3) { total += dealDamageToEnemy(card.value + player.strength) }
                "Triple Strike for $total damage."
            }
            EffectType.STUN -> {
                enemy.statuses.add(StatusEffect(EffectType.STUN, 1))
                "Stunned the enemy."
            }
            EffectType.BLEED -> {
                enemy.statuses.add(StatusEffect(EffectType.BLEED, 3, 1))
                "Applied Bleed."
            }
            else -> "Played ${card.name}."
        }
        deckManager.removeFromHand(card)
        return log
    }

    fun endPlayerTurn(): String {
        // Trigger player end-turn effects
        processStatuses(player)
        
        if (player.hp <= 0) return "Player defeated."
        
        // Enemy Turn
        val log = executeEnemyAI()
        
        // Reset player for next turn
        player.block = 0
        player.energy = player.baseEnergy
        deckManager.drawCards(5 + GameSession.drawBonusPerTurn)
        
        return log
    }

    private fun executeEnemyAI(): String {
        // Check for Stun
        val stun = enemy.statuses.find { it.type == EffectType.STUN }
        if (stun != null) {
            stun.duration--
            if (stun.duration <= 0) enemy.statuses.remove(stun)
            return "${enemy.name} is Stunned and skips turn!"
        }

        processStatuses(enemy)
        if (enemy.hp <= 0) return "Enemy defeated by status effects."

        // More complex AI Strategy
        return when (enemy.id) {
            "BANDITS" -> executeBanditAI()
            "RIVAL_GENERAL" -> executeGeneralAI()
            "ARMY" -> executeArmyAI()
            else -> if (enemy.isBoss) executeBossAI() else executeBasicAI()
        }
    }

    private fun executeBanditAI(): String {
        val rand = Random.nextInt(3)
        return when (rand) {
            0 -> {
                val dmg = dealDamageToPlayer(1)
                player.energy = max(0, player.energy - 1)
                "Bandit steals your focus! $dmg damage and -1 Energy."
            }
            1 -> {
                var total = 0
                repeat(2) { total += dealDamageToPlayer(1) }
                "Bandit multi-strikes! $total damage."
            }
            else -> {
                enemy.block += 1
                "Bandit hides in the shadows (Block +1)."
            }
        }
    }

    private fun executeGeneralAI(): String {
        val rand = Random.nextInt(3)
        return when (rand) {
            0 -> {
                enemyStrength += 1
                "Rival General rallies! Strength +1."
            }
            1 -> {
                val dmg = dealDamageToPlayer(enemy.damage + enemyStrength)
                "Rival General executes a precise strike for $dmg."
            }
            else -> {
                player.statuses.add(StatusEffect(EffectType.BLEED, 2, 1))
                "Rival General causes you to bleed!"
            }
        }
    }

    private fun executeArmyAI(): String {
        if (player.block == 0 && Random.nextBoolean()) {
            val dmg = dealDamageToPlayer(enemy.damage + 2)
            return "Vanguard coordinated strike! Heavy $dmg damage since you were unguarded."
        }
        enemy.block += 2
        return "Army forms a shield wall! Block +2."
    }

    private fun executeBossAI(): String {
        val hpRatio = enemy.hp.toDouble() / enemy.maxHp.toDouble()
        if (hpRatio < 0.3 && enemy.block < 2) {
            enemy.block += 3
            return "${enemy.name} goes into a desperate defense! Block +3."
        }
        
        return when (enemy.id) {
            "BOSS_LU_BU" -> {
                if (hpRatio < 0.5) {
                    val dmg = dealDamageToPlayer(enemy.damage + 2)
                    "${enemy.name} is ENRAGED! Massive swing for $dmg damage."
                } else {
                    val dmg = dealDamageToPlayer(enemy.damage)
                    player.statuses.add(StatusEffect(EffectType.BLEED, 3, 1))
                    "${enemy.name} strikes and leaves a deep wound."
                }
            }
            "BOSS_CAO_CAO" -> {
                if (Random.nextBoolean()) {
                    player.energy = max(0, player.energy - 2)
                    "Cao Cao uses dark schemes! You lose 2 Energy."
                } else {
                    val dmg = dealDamageToPlayer(enemy.damage)
                    "Cao Cao suppresses you for $dmg damage."
                }
            }
            else -> executeBasicAI()
        }
    }

    private fun executeBasicAI(): String {
        val dmg = dealDamageToPlayer(enemy.damage)
        return "${enemy.name} attacks for $dmg damage."
    }

    private fun dealDamageToPlayer(rawDamage: Int): Int {
        val incoming = (rawDamage * (1.0 - GameSession.damageReduction)).toInt()
        val actual = max(0, incoming - player.block)
        player.hp -= actual
        player.block = max(0, player.block - incoming)
        return actual
    }

    private fun processStatuses(entity: Any) {
        val statuses = if (entity is Player) entity.statuses else (entity as Enemy).statuses
        val iterator = statuses.iterator()
        while (iterator.hasNext()) {
            val status = iterator.next()
            when (status.type) {
                EffectType.BURN_STRIKE -> {
                    if (entity is Player) entity.hp -= status.value else (entity as Enemy).hp -= status.value
                }
                EffectType.BLEED -> {
                    if (entity is Player) entity.hp -= status.value else (entity as Enemy).hp -= status.value
                }
                else -> {}
            }
            status.duration--
            if (status.duration <= 0) iterator.remove()
        }
    }

    private fun dealDamageToEnemy(rawDamage: Int): Int {
        var damageMultiplier = GameSession.attackMultiplier
        
        // Rage/Status check
        player.statuses.find { it.type == EffectType.RAGE_BOOST }?.let {
            damageMultiplier += (it.value * 0.5)
        }
        
        if (GameSession.selectedCharacter == CharacterId.LU_BU) {
            val missingHp = player.maxHp - player.hp
            damageMultiplier += (missingHp * 0.2)
        }

        val crit = if (Random.nextDouble() < GameSession.critChance) GameSession.critDamageMultiplier else 1.0
        val scaledDamage = (rawDamage * damageMultiplier * crit).toInt()
        val actual = max(0, scaledDamage - enemy.block)
        
        enemy.hp -= actual
        enemy.block = max(0, enemy.block - scaledDamage)
        return actual
    }
}
