package com.rogie.threekingdoms.game

import com.rogie.threekingdoms.model.Card
import com.rogie.threekingdoms.model.Enemy
import com.rogie.threekingdoms.model.Player
import com.rogie.threekingdoms.meta.Character
import com.rogie.threekingdoms.meta.CharacterId
import kotlin.random.Random

object GameSession {
    lateinit var player: Player
    lateinit var character: Character
    var deck: MutableList<Card> = mutableListOf()
    lateinit var selectedCharacter: CharacterId
    var selectedTalents: Set<String> = emptySet()
    var encounterCount: Int = 0
    var hasImperialSeal: Boolean = false
    var enemiesDefeated: Int = 0
    var bossesDefeated: Int = 0
    var chapterReached: Int = 1
    var pendingUnlock: CharacterId? = null
    var attackMultiplier: Double = 1.0
    var critDamageMultiplier: Double = 1.5
    var critChance: Double = 0.1
    var strategyMultiplier: Double = 1.0
    var drawBonusPerTurn: Int = 0
    var skillDiscountEnabled: Boolean = false
    var discountOncePerTurn: Boolean = false
    var drainBonusHeal: Int = 0
    var damageReduction: Double = 0.0
    var bonusGoldPerVictory: Int = 0
    var energyOnKill: Int = 0
    var extraStartBlock: Int = 0
    var luBuSelfHeartLossStart: Boolean = false
    var firstAttackBonusPerTurn: Double = 0.0

    fun startRun(selected: Character, totalUpgradeLevels: Int, unlockedTalents: Set<String>) {
        selectedCharacter = selected.id
        character = selected
        selectedTalents = unlockedTalents
        player = Player(faction = selected.faction)
        player.baseEnergy = selected.energy
        player.energy = selected.energy
        player.maxHp = selected.maxHp
        player.hp = selected.maxHp
        deck = CardLibrary.starterDeck(selected.id).toMutableList()
        encounterCount = 0
        hasImperialSeal = false
        enemiesDefeated = 0
        bossesDefeated = 0
        chapterReached = 1
        pendingUnlock = null

        attackMultiplier = 1.0
        critDamageMultiplier = 1.5
        critChance = selected.critChance
        strategyMultiplier = 1.0
        drawBonusPerTurn = 0
        skillDiscountEnabled = false
        discountOncePerTurn = false
        drainBonusHeal = 0
        damageReduction = selected.defense * 0.01
        bonusGoldPerVictory = 0
        energyOnKill = 0
        extraStartBlock = 0
        luBuSelfHeartLossStart = false
        firstAttackBonusPerTurn = 0.0

        applyCharacterPassive()
        applyTalents()

        // Health scaling adjusted for 1 HP per heart
        val hpBonusFromProgress = totalUpgradeLevels / 5
        player.maxHp += hpBonusFromProgress
        player.hp = player.maxHp
    }

    fun onEnemyDefeated(enemy: Enemy) {
        enemiesDefeated++
        if (enemy.isBoss) {
            bossesDefeated++
            // Unlock logic based on boss defeat
            if (enemy.id == "BOSS_LU_BU") pendingUnlock = CharacterId.LU_BU
            if (enemy.id == "BOSS_CAO_CAO") pendingUnlock = CharacterId.CAO_CAO
            if (enemy.id == "BOSS_ZHOU_YU") pendingUnlock = CharacterId.ZHOU_YU
        }
    }

    fun addCard(card: Card) {
        deck.add(card)
    }

    fun buildEnemy(): Enemy {
        encounterCount++
        chapterReached = maxOf(chapterReached, ((encounterCount - 1) / 5) + 1)
        val isBoss = encounterCount % 5 == 0
        return if (isBoss) {
            when (Random.nextInt(4)) {
                0 -> Enemy("BOSS_LU_BU", "Lu Bu (Boss)", 10, 10, 2, isBoss = true)
                1 -> Enemy("BOSS_DONG_ZHUO", "Dong Zhuo (Boss)", 11, 11, 1, isBoss = true)
                2 -> Enemy("BOSS_YUAN_SHAO", "Yuan Shao (Boss)", 12, 12, 1, isBoss = true)
                else -> Enemy("BOSS_CAO_CAO", "Cao Cao (Boss)", 10, 10, 2, isBoss = true)
            }
        } else {
            when (Random.nextInt(3)) {
                0 -> Enemy("BANDITS", "Bandit Raiders", 3, 3, 1)
                1 -> Enemy("RIVAL_GENERAL", "Rival General", 4, 4, 1)
                else -> Enemy("ARMY", "Frontline Army", 5, 5, 1)
            }
        }
    }

    private fun applyCharacterPassive() {
        when (selectedCharacter) {
            CharacterId.GUAN_YU -> attackMultiplier += 0.10
            CharacterId.ZHUGE_LIANG -> drawBonusPerTurn += 1
            CharacterId.CAO_CAO -> drainBonusHeal += 1 // Nerfed
            CharacterId.ZHOU_YU -> strategyMultiplier += 0.15
            CharacterId.LU_BU -> attackMultiplier += 0.20
        }
        player.hp = player.maxHp
    }

    private fun applyTalents() {
        selectedTalents.forEach { talentId ->
            when (talentId) {
                "gy_off_1" -> attackMultiplier += 0.20
                "gy_off_2" -> critDamageMultiplier = 2.0
                "gy_off_3" -> energyOnKill += 1
                "gy_def_1" -> player.maxHp += 1 // Nerfed to 1 HP
                "gy_def_2" -> extraStartBlock += 1
                "gy_def_3" -> damageReduction += 0.10
                "gy_utl_1" -> player.baseEnergy += 1
                "gy_utl_2" -> drawBonusPerTurn += 1
                "gy_utl_3" -> bonusGoldPerVictory += 10
                "gy_utl_4" -> firstAttackBonusPerTurn += 0.25

                "zl_off_1" -> attackMultiplier += 0.15
                "zl_off_2" -> strategyMultiplier += 0.25
                "zl_off_3" -> critChance += 0.10
                "zl_def_1" -> player.maxHp += 1
                "zl_def_2" -> extraStartBlock += 1
                "zl_def_3" -> damageReduction += 0.08
                "zl_utl_1" -> drawBonusPerTurn += 1
                "zl_utl_2" -> {
                    skillDiscountEnabled = true
                    discountOncePerTurn = true
                }
                "zl_utl_3" -> strategyMultiplier += 0.30
                "zl_utl_4" -> player.baseEnergy += 1

                "cc_off_1" -> attackMultiplier += 0.15
                "cc_off_2" -> attackMultiplier += 0.10
                "cc_off_3" -> critChance += 0.12
                "cc_def_1" -> player.maxHp += 1
                "cc_def_2" -> drainBonusHeal += 1
                "cc_def_3" -> damageReduction += 0.12
                "cc_utl_1" -> player.baseEnergy += 1
                "cc_utl_2" -> bonusGoldPerVictory += 15
                "cc_utl_3" -> skillDiscountEnabled = true
                "cc_utl_4" -> attackMultiplier += 0.10

                "zy_off_1" -> strategyMultiplier += 0.20
                "zy_off_2" -> strategyMultiplier += 0.15
                "zy_off_3" -> attackMultiplier += 0.10
                "zy_def_1" -> player.maxHp += 1
                "zy_def_2" -> extraStartBlock += 1
                "zy_def_3" -> damageReduction += 0.10
                "zy_utl_1" -> drawBonusPerTurn += 1
                "zy_utl_2" -> skillDiscountEnabled = true
                "zy_utl_3" -> strategyMultiplier += 0.25
                "zy_utl_4" -> bonusGoldPerVictory += 12

                "lb_off_1" -> attackMultiplier += 0.25
                "lb_off_2" -> attackMultiplier += 0.15
                "lb_off_3" -> attackMultiplier += 0.20
                "lb_def_1" -> player.maxHp += 1
                "lb_def_2" -> damageReduction += 0.08
                "lb_def_3" -> extraStartBlock += 1
                "lb_utl_1" -> player.baseEnergy += 1
                "lb_utl_2" -> energyOnKill += 1
                "lb_utl_3" -> bonusGoldPerVictory += 20
                "lb_utl_4" -> {
                    attackMultiplier += 0.20
                    luBuSelfHeartLossStart = true
                }
            }
        }
        player.energy = player.baseEnergy
    }
}
