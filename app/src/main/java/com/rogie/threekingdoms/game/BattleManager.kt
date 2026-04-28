package com.rogie.threekingdoms.game

import com.rogie.threekingdoms.model.Card
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
    private var isPlayerTurn = true
    private var enemyStrength = 0
    private val playedTags = mutableListOf<String>()
    private var attackedLastTurn = false

    fun startBattle() {
        player.energy = player.baseEnergy
        player.block = 0
        player.strength = 0
        player.honor = 0
        player.rage = 0
        player.insight = 0
        enemyStrength = 0
        attackedLastTurn = false
        playedTags.clear()
        player.statuses.clear()
        enemy.statuses.clear()
        
        isPlayerTurn = player.speed >= enemy.speed
        player.block += GameSession.extraStartBlock
        
        if (GameSession.selectedCharacter == CharacterId.LU_BU && GameSession.luBuSelfHeartLossStart) {
            player.hp = max(1, player.hp - 1)
        }
        
        if (!isPlayerTurn) {
            executeEnemyAI()
            isPlayerTurn = true
        }
        deckManager.drawCards(5 + GameSession.drawBonusPerTurn)
    }

    fun useSkill(skill: Skill): String {
        if (player.energy < skill.energyCost) return "Not enough energy for ${skill.name}."
        player.energy -= skill.energyCost
        return resolveSkillEffect(skill)
    }

    fun playerPlayCard(card: Card): String {
        if (card.energyCost > player.energy) return "Not enough energy."
        player.energy -= card.energyCost
        
        playedTags.addAll(card.tags)
        val comboLog = checkCombo()

        val log = when (card.effect) {
            EffectType.DAMAGE -> {
                var dmg = card.value + player.strength
                if (card.id == "gy_slash_1" && player.honor >= 2) dmg += 4
                if (card.id == "gy_right" && attackedLastTurn) dmg *= 2
                if (card.id == "gy_u_oath") dmg = player.honor * 6
                
                val dealt = dealDamageToEnemy(dmg)
                "${card.name} dealt $dealt damage."
            }
            EffectType.BLOCK -> {
                var blockVal = card.value
                if (card.id == "gy_shield") blockVal = player.honor * 2
                player.block += blockVal
                
                if (card.tags.contains("[Counter]")) {
                    val counterVal = if (card.id == "gy_guard") 5 else 1
                    player.statuses.add(StatusEffect(EffectType.COUNTER, 1, counterVal))
                }
                "${card.name} raised defenses."
            }
            EffectType.HONOR_BOOST -> {
                player.honor += card.value
                "Honor increased by ${card.value}."
            }
            EffectType.BUFF_STRENGTH -> {
                if (card.id == "gy_calm") {
                    player.statuses.add(StatusEffect(EffectType.BUFF_STRENGTH, 1, 50))
                    "Calm Before Strike: Next attack +50% damage."
                } else if (card.id == "gy_u_counter") {
                    player.statuses.add(StatusEffect(EffectType.COUNTER, 1, 50)) // Logic for bonus counter dmg
                    "Counter Flow: Counter damage increased."
                } else {
                    player.strength += card.value
                    "Strength increased."
                }
            }
            EffectType.HEAL -> {
                player.hp = minOf(player.maxHp, player.hp + card.value)
                if (card.id == "gy_loyal" && player.honor >= 3) deckManager.drawCards(1)
                "Healed ${card.value} HP."
            }
            EffectType.EXECUTE -> {
                // Judgment Cut: Ignore Defense
                val actual = max(0, (card.value + player.strength) * 1) 
                enemy.hp -= actual
                "${card.name} ignored defense for $actual damage."
            }
            EffectType.MULTI_HIT -> {
                val hits = if (card.id == "gy_u_twin" && player.honor >= 2) 3 else 2
                var total = 0
                repeat(hits) { total += dealDamageToEnemy(card.value + player.strength) }
                "${card.name} hit $hits times for $total damage."
            }
            EffectType.IMMUNITY -> {
                val dmg = dealDamageToEnemy(player.honor * 8)
                player.honor = 0
                player.statuses.add(StatusEffect(EffectType.IMMUNITY, 1, 1))
                "SAINT OF WAR! Dealt $dmg and gained Immunity!"
            }
            else -> "Played ${card.name}."
        }
        
        deckManager.removeFromHand(card)
        return if (comboLog.isNotEmpty()) "$log\nCOMBO: $comboLog" else log
    }

    private fun checkCombo(): String {
        val last2 = playedTags.takeLast(2)
        if (last2 == listOf("[Fire]", "[Wind]")) {
            enemy.statuses.add(StatusEffect(EffectType.BURN_STRIKE, 4, 1))
            return "INFERNO! Burn increased."
        }
        return ""
    }

    private fun resolveSkillEffect(skill: Skill): String {
        return when (skill.name) {
            "Godly Strike" -> "Dealt ${dealDamageToEnemy(2 + player.strength)} damage."
            "Wind's Call" -> { enemy.statuses.add(StatusEffect(EffectType.STUN, 1)); "Stunned enemy." }
            else -> "Skill used."
        }
    }

    fun endPlayerTurn(): String {
        processStatuses(player)
        if (player.hp <= 0) return "Player defeated."
        attackedLastTurn = false
        val log = executeEnemyAI()
        player.block = 0
        player.energy = player.baseEnergy
        playedTags.clear() 
        deckManager.drawCards(5 + GameSession.drawBonusPerTurn)
        return log
    }

    private fun executeEnemyAI(): String {
        val stun = enemy.statuses.find { it.type == EffectType.STUN }
        if (stun != null) {
            stun.duration--
            if (stun.duration <= 0) enemy.statuses.remove(stun)
            return "${enemy.name} is Stunned!"
        }

        processStatuses(enemy)
        if (enemy.hp <= 0) return "Enemy defeated by status effects."

        return when (enemy.id) {
            "BANDITS" -> {
                val dmg = dealDamageToPlayer(2)
                player.energy = max(0, player.energy - 1)
                "Bandit Raider deals $dmg and drains 1 Energy!"
            }
            "RIVAL_GENERAL" -> {
                if (Random.nextBoolean()) {
                    enemyStrength += 1
                    "Rival General rallies! Strength +1."
                } else {
                    val dmg = dealDamageToPlayer(3 + enemyStrength)
                    "Rival General strikes for $dmg!"
                }
            }
            "ARMY" -> {
                enemy.block += 5
                val dmg = dealDamageToPlayer(4)
                "Vanguard forms a Shield Wall and strikes for $dmg!"
            }
            else -> {
                val dmg = dealDamageToPlayer(enemy.damage)
                "${enemy.name} attacks for $dmg."
            }
        }
    }

    private fun dealDamageToPlayer(rawDamage: Int): Int {
        attackedLastTurn = true
        val incoming = (rawDamage * (1.0 - GameSession.damageReduction)).toInt()
        val actual = max(0, incoming - player.block)
        
        val counter = player.statuses.find { it.type == EffectType.COUNTER }
        if (counter != null && actual < incoming) {
            var counterDmg = counter.value + player.strength + (player.honor / 2)
            if (player.statuses.any { it.type == EffectType.COUNTER && it.value == 50 }) counterDmg = (counterDmg * 1.5).toInt()
            dealDamageToEnemy(counterDmg)
        }

        player.hp -= actual
        player.block = max(0, player.block - incoming)
        return actual
    }

    private fun processStatuses(entity: Any) {
        val statuses = if (entity is Player) entity.statuses else (entity as Enemy).statuses
        val iterator = statuses.iterator()
        while (iterator.hasNext()) {
            val status = iterator.next()
            if (status.type == EffectType.BURN_STRIKE || status.type == EffectType.BLEED) {
                if (entity is Player) entity.hp -= status.value else (entity as Enemy).hp -= status.value
            }
            status.duration--
            if (status.duration <= 0) iterator.remove()
        }
    }

    private fun dealDamageToEnemy(rawDamage: Int): Int {
        if (player.statuses.any { it.type == EffectType.IMMUNITY }) return 0 
        
        var damageMultiplier = GameSession.attackMultiplier
        if (GameSession.selectedCharacter == CharacterId.GUAN_YU) {
            damageMultiplier += (player.honor / 2) * 0.2 // Reduced passive from 0.5 to 0.2
        }
        
        player.statuses.find { it.type == EffectType.BUFF_STRENGTH && it.value == 50 }?.let {
            damageMultiplier += 0.5
            player.statuses.remove(it)
        }
        
        val scaledDamage = (rawDamage * damageMultiplier).toInt()
        val actual = max(0, scaledDamage - enemy.block)
        enemy.hp -= actual
        enemy.block = max(0, enemy.block - scaledDamage)
        return actual
    }
}
