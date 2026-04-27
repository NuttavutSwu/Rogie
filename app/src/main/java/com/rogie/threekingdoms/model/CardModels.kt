package com.rogie.threekingdoms.model

enum class CardType {
    ATTACK, DEFENSE, SKILL, SPECIAL, TRANSFORMATION
}

enum class CardRarity {
    COMMON, RARE, EPIC, LEGENDARY
}

enum class EffectType {
    DAMAGE, BLOCK, BURN_STRIKE, BUFF_STRENGTH, DRAIN, MULTI_HIT,
    STUN, STEAL, DRAW_CARDS, BLEED, RAGE_BOOST, HEAL, STANCE_CHANGE,
    EXECUTE, REFLECT, REDUCE_COST, LOW_HP_BUFF, TRANSFORM
}

data class Card(
    val id: String = "",
    val name: String,
    val energyCost: Int,
    val description: String,
    val type: CardType,
    val effect: EffectType,
    val value: Int,
    val rarity: CardRarity = CardRarity.COMMON,
    val tags: List<String> = emptyList()
)

enum class Faction {
    SHU, WEI, WU, OTHER
}

data class StatusEffect(
    val type: EffectType,
    var duration: Int,
    val value: Int = 0
)

data class Player(
    val faction: Faction,
    var hp: Int = 5,
    var maxHp: Int = 5,
    var block: Int = 0,
    var baseEnergy: Int = 3,
    var energy: Int = 3,
    var strength: Int = 0,
    var rage: Int = 0,
    var gold: Int = 0,
    var speed: Int = 10,
    val statuses: MutableList<StatusEffect> = mutableListOf(),
    var weapon: Equipment? = null,
    var armor: Equipment? = null,
    var mount: Equipment? = null,
    var treasure: Equipment? = null
)

data class Enemy(
    val id: String,
    val name: String,
    var hp: Int,
    var maxHp: Int,
    var damage: Int,
    val isBoss: Boolean = false,
    var block: Int = 0,
    var speed: Int = 8,
    val statuses: MutableList<StatusEffect> = mutableListOf(),
    var phase: Int = 1
)

enum class EquipmentType {
    WEAPON, ARMOR, MOUNT, TREASURE
}

data class Equipment(
    val id: String,
    val name: String,
    val type: EquipmentType,
    val description: String,
    val atkBonus: Int = 0,
    val defBonus: Int = 0,
    val speedBonus: Int = 0,
    val effect: EffectType? = null,
    val effectValue: Int = 0
)
