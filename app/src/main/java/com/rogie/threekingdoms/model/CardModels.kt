package com.rogie.threekingdoms.model

enum class CardType {
    ATTACK, DEFENSE, SKILL, STRATEGY
}

enum class EffectType {
    DAMAGE, BLOCK, BURN_STRIKE, BUFF_STRENGTH, DRAIN, MULTI_HIT
}

data class Card(
    val name: String,
    val energyCost: Int,
    val description: String,
    val type: CardType,
    val effect: EffectType,
    val value: Int,
    val rarity: Int = 1
)

enum class Faction {
    SHU, WEI, WU
}

data class Player(
    val faction: Faction,
    var hp: Int = 80,
    var maxHp: Int = 80,
    var block: Int = 0,
    var baseEnergy: Int = 3,
    var energy: Int = 3,
    var strength: Int = 0,
    var gold: Int = 0
)

data class Enemy(
    val id: String,
    val name: String,
    var hp: Int,
    var maxHp: Int,
    var damage: Int,
    val isBoss: Boolean = false,
    var block: Int = 0,
    var burn: Int = 0
)
