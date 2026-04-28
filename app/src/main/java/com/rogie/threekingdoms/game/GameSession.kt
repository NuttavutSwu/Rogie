package com.rogie.threekingdoms.game

import com.rogie.threekingdoms.model.Card
import com.rogie.threekingdoms.model.Enemy
import com.rogie.threekingdoms.model.Player
import com.rogie.threekingdoms.model.Equipment
import com.rogie.threekingdoms.meta.Character
import com.rogie.threekingdoms.meta.CharacterId
import kotlin.random.Random

object GameSession {
    lateinit var player: Player
    lateinit var character: Character
    var deck: MutableList<Card> = mutableListOf()
    var inventory: MutableList<Equipment> = mutableListOf()
    lateinit var selectedCharacter: CharacterId
    var selectedTalents: Set<String> = emptySet()
    
    // --- Story & Progression Stats ---
    var chaosLevel: Int = 0
    var honorLevel: Int = 0
    var spareCount: Int = 0
    var executeCount: Int = 0
    var secretBossesDefeated: Int = 0
    var currentChapter: Int = 1
    var encounterCount: Int = 0
    var isMainStoryEvent: Boolean = true
    var needsEpilogue: Boolean = false // ตัวแปรใหม่สำหรับเนื้อเรื่องจบบท
    var lastBossDefeatedId: String? = null
    
    var enemiesDefeated: Int = 0
    var bossesDefeated: Int = 0
    var pendingUnlock: CharacterId? = null
    var hasImperialSeal: Boolean = false

    val hiddenFlags = mutableMapOf<String, Boolean>()
    val counterFlags = mutableMapOf<String, Int>()
    
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
    
    private var nextForcedEnemy: Enemy? = null

    fun startRun(selected: Character, totalUpgradeLevels: Int, unlockedTalents: Set<String>) {
        selectedCharacter = selected.id
        character = selected
        selectedTalents = unlockedTalents
        player = Player(faction = selected.faction)
        player.baseEnergy = selected.energy
        player.energy = selected.energy
        player.maxHp = selected.maxHp
        player.hp = selected.maxHp
        player.speed = selected.speed
        deck = CardLibrary.starterDeck(selected.id).toMutableList()
        inventory = mutableListOf()
        
        chaosLevel = 0
        honorLevel = 0
        spareCount = 0
        executeCount = 0
        secretBossesDefeated = 0
        currentChapter = 1
        encounterCount = 0
        isMainStoryEvent = true
        needsEpilogue = false
        lastBossDefeatedId = null
        
        enemiesDefeated = 0
        bossesDefeated = 0
        pendingUnlock = null
        nextForcedEnemy = null
        hasImperialSeal = false
        hiddenFlags.clear()
        counterFlags.clear()

        attackMultiplier = 1.0
        critDamageMultiplier = 1.5
        critChance = selected.critChance
        strategyMultiplier = 1.0
        drawBonusPerTurn = 0
        skillDiscountEnabled = false
        discountOncePerTurn = false
        drainBonusHeal = 0
        damageReduction = selected.defense * 0.02
        bonusGoldPerVictory = 0
        energyOnKill = 0
        extraStartBlock = 0
        luBuSelfHeartLossStart = false
        firstAttackBonusPerTurn = 0.0

        applyCharacterPassive()
        applyTalents()

        val hpBonusFromProgress = totalUpgradeLevels / 8
        player.maxHp += hpBonusFromProgress
        player.hp = player.maxHp
    }

    fun setForcedEnemy(enemy: Enemy) {
        nextForcedEnemy = enemy
    }

    fun onEnemyDefeated(enemy: Enemy) {
        enemiesDefeated++
        if (enemy.isBoss) {
            bossesDefeated++
            lastBossDefeatedId = enemy.id
            needsEpilogue = true // เตรียมแสดงเนื้อเรื่องปิดบท
            
            if (enemy.id.startsWith("SECRET")) secretBossesDefeated++
            if (enemy.id == "BOSS_LU_BU") pendingUnlock = CharacterId.LU_BU
            if (enemy.id == "BOSS_CAO_CAO") pendingUnlock = CharacterId.CAO_CAO
            if (enemy.id == "BOSS_ZHOU_YU") pendingUnlock = CharacterId.ZHOU_YU
        }
    }

    fun addCard(card: Card) {
        deck.add(card)
    }

    fun buildEnemy(): Enemy {
        nextForcedEnemy?.let {
            val e = it
            nextForcedEnemy = null
            return e
        }

        if (encounterCount > 0 && encounterCount % 4 == 0) {
            val secretBoss = checkSecretBossTriggers()
            if (secretBoss != null) return secretBoss
        }

        encounterCount++
        if (currentChapter >= 5) {
            return buildFinalBoss(1.0 + (encounterCount * 0.2))
        }

        // ปรับให้สู้บอสทุกๆ 8 ด่าน เพื่อให้มีเหตุการณ์สุ่มเยอะขึ้น
        val isBoss = encounterCount % 8 == 0
        val scale = 1.0 + (encounterCount * 0.25)
        
        return if (isBoss) {
            when (currentChapter) {
                1 -> Enemy("CORRUPTED_GENERAL", "แม่ทัพผู้แปดเปื้อน", (35 * scale).toInt(), (35 * scale).toInt(), 3, isBoss = true, speed = 10)
                2 -> Enemy("FLAME_WARLORD", "ขุนพลอัคคี", (50 * scale).toInt(), (50 * scale).toInt(), 4, isBoss = true, speed = 12)
                3 -> Enemy("ILLUSION_KEEPER", "ผู้พิทักษ์ภาพลวงตา", (65 * scale).toInt(), (65 * scale).toInt(), 5, isBoss = true, speed = 13)
                else -> Enemy("BOSS_LU_BU", "ลิโป้ จอมราชันย์ล่มสลาย", (90 * scale).toInt(), (90 * scale).toInt(), 6, isBoss = true, speed = 15)
            }
        } else {
            when (Random.nextInt(3)) {
                0 -> Enemy("BANDITS", "กองโจรป่า", (12 * scale).toInt(), (12 * scale).toInt(), 2, speed = 9)
                1 -> Enemy("RIVAL_GENERAL", "ขุนพลฝ่ายศัตรู", (18 * scale).toInt(), (18 * scale).toInt(), 3, speed = 11)
                else -> Enemy("ARMY", "กองทัพหน้า", (22 * scale).toInt(), (22 * scale).toInt(), 4, speed = 10)
            }
        }
    }

    private fun checkSecretBossTriggers(): Enemy? {
        val scale = 1.0 + (encounterCount * 0.3)
        // ... (Logic เดิม)
        if (hiddenFlags["shrine_prayed"] == true && !hiddenFlags.containsKey("boss_spirit_guardian")) {
            hiddenFlags["boss_spirit_guardian"] = true
            return Enemy("SECRET_SPIRIT_GUARDIAN", "องครักษ์จิตวิญญาณ", (50 * scale).toInt(), (50 * scale).toInt(), 5, isBoss = true, speed = 14)
        }
        return null
    }

    private fun buildFinalBoss(scale: Double): Enemy {
        return when {
            spareCount >= 3 && secretBossesDefeated >= 2 -> 
                Enemy("TRUE_BOSS_DRAGON", "มังกรแห่งโชคชะตา", (150 * scale).toInt(), (150 * scale).toInt(), 10, isBoss = true, speed = 20)
            spareCount > executeCount -> 
                Enemy("FALLEN_GUARDIAN", "ผู้พิทักษ์ที่ร่วงหล่น", (100 * scale).toInt(), (100 * scale).toInt(), 7, isBoss = true, speed = 15)
            else -> 
                Enemy("CHAOS_INCARNATE", "อวตารแห่งความบ้าคลั่ง", (120 * scale).toInt(), (120 * scale).toInt(), 8, isBoss = true, speed = 18)
        }
    }

    private fun applyCharacterPassive() {
        when (selectedCharacter) {
            CharacterId.GUAN_YU -> attackMultiplier += 0.10
            CharacterId.ZHUGE_LIANG -> drawBonusPerTurn += 1
            CharacterId.CAO_CAO -> drainBonusHeal += 1
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
                "gy_def_1" -> player.maxHp += 1
                "gy_def_2" -> extraStartBlock += 1
                "gy_utl_1" -> player.baseEnergy += 1
                "zl_off_1" -> attackMultiplier += 0.15
                "zl_def_1" -> player.maxHp += 1
                "zl_utl_1" -> drawBonusPerTurn += 1
                "cc_off_1" -> attackMultiplier += 0.15
                "cc_def_1" -> player.maxHp += 1
                "cc_utl_1" -> player.baseEnergy += 1
                "zy_off_1" -> strategyMultiplier += 0.20
                "zy_def_1" -> player.maxHp += 1
                "zy_utl_1" -> drawBonusPerTurn += 1
                "lb_off_1" -> attackMultiplier += 0.25
                "lb_def_1" -> player.maxHp += 1
                "lb_utl_1" -> player.baseEnergy += 1
            }
        }
        player.energy = player.baseEnergy
    }
}
