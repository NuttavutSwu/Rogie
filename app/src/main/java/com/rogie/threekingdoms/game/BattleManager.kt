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
    private val playedTags = mutableListOf<String>()

    fun startBattle() {
        player.energy = player.baseEnergy
        player.block = 0
        player.strength = 0
        enemyStrength = 0
        playedTags.clear()
        player.statuses.clear()
        enemy.statuses.clear()
        
        isPlayerTurn = player.speed >= enemy.speed
        
        usedDiscountThisTurn = false
        usedFirstAttackBonusThisTurn = false
        
        if (GameSession.selectedCharacter == CharacterId.LU_BU && GameSession.luBuSelfHeartLossStart) {
            player.hp = max(1, player.hp - 1)
        }
        player.block += GameSession.extraStartBlock
        
        if (!isPlayerTurn) {
            executeEnemyAI()
            isPlayerTurn = true
        }
        deckManager.drawCards(2 + GameSession.drawBonusPerTurn)
    }

    fun useSkill(skill: Skill): String {
        if (player.energy < skill.energyCost) return "Not enough energy for ${skill.name}."
        player.energy -= skill.energyCost
        return resolveSkillEffect(skill)
    }

    fun playerPlayCard(card: Card): String {
        if (card.energyCost > player.energy) return "Not enough energy."
        player.energy -= card.energyCost
        
        // Track combo tags
        playedTags.addAll(card.tags)
        val comboLog = checkCombo()

        val log = when (card.effect) {
            EffectType.DAMAGE -> {
                val bonus = if (GameSession.selectedCharacter == CharacterId.GUAN_YU && card.tags.contains("[Slash]")) 1 else 0
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
            else -> "Played ${card.name}."
        }
        
        return if (comboLog.isNotEmpty()) "$log\nCOMBO: $comboLog" else log
    }

    private fun checkCombo(): String {
        val last3 = playedTags.takeLast(3)
        val last2 = playedTags.takeLast(2)

        // [Slash] -> [Slash] -> [Heavy] = Finisher
        if (last3 == listOf("[Slash]", "[Slash]", "[Slash]")) { // Simplified for demo
            dealDamageToEnemy(3)
            playedTags.clear()
            return "FINISHER! Massive damage dealt."
        }

        // [Fire] -> [Wind] = Burn Spread
        if (last2 == listOf("[Fire]", "[Wind]")) {
            enemy.statuses.add(StatusEffect(EffectType.BURN_STRIKE, 4, 1))
            return "INFERNO! Burn increased."
        }

        // [Strategy] -> [Strategy] = Prep
        if (last2 == listOf("[Strategy]", "[Strategy]")) {
            player.energy = minOf(player.baseEnergy, player.energy + 1)
            return "PREPARATION! Gained 1 Energy."
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
        val log = executeEnemyAI()
        player.block = 0
        player.energy = player.baseEnergy
        playedTags.clear() // Reset combo on turn end
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
        if (enemy.hp <= 0) return "Enemy defeated."
        
        val dmg = dealDamageToPlayer(enemy.damage)
        return "${enemy.name} attacks for $dmg."
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
            if (status.type == EffectType.BURN_STRIKE || status.type == EffectType.BLEED) {
                if (entity is Player) entity.hp -= status.value else (entity as Enemy).hp -= status.value
            }
            status.duration--
            if (status.duration <= 0) iterator.remove()
        }
    }

    private fun dealDamageToEnemy(rawDamage: Int): Int {
        var damageMultiplier = GameSession.attackMultiplier
        player.statuses.find { it.type == EffectType.RAGE_BOOST }?.let { damageMultiplier += (it.value * 0.5) }
        val scaledDamage = (rawDamage * damageMultiplier).toInt()
        val actual = max(0, scaledDamage - enemy.block)
        enemy.hp -= actual
        enemy.block = max(0, enemy.block - scaledDamage)
        return actual
    }
}
