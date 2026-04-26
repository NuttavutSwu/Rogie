package com.rogie.threekingdoms.meta

import com.rogie.threekingdoms.model.Faction

enum class CharacterId {
    GUAN_YU, ZHUGE_LIANG, CAO_CAO, ZHOU_YU, LU_BU
}

data class Skill(
    val name: String,
    val description: String,
    val energyCost: Int,
    val isUltimate: Boolean = false
)

data class Character(
    val id: CharacterId,
    val displayName: String,
    val faction: Faction,
    val passiveName: String,
    val passiveDescription: String,
    val activeSkill: Skill,
    val ultimateSkill: Skill,
    val maxHearts: Int,
    val attack: Int,
    val defense: Int,
    val speed: Int,
    val energy: Int,
    val critChance: Double
) {
    val maxHp: Int get() = maxHearts
}

enum class TalentPath {
    OFFENSE, DEFENSE, UTILITY
}

enum class TalentRarity {
    COMMON, RARE, LEGENDARY
}

data class TalentNode(
    val id: String,
    val name: String,
    val path: TalentPath,
    val description: String,
    val cost: Int,
    val effectValue: Double = 0.0,
    val requires: List<String> = emptyList(),
    val rarity: TalentRarity = TalentRarity.COMMON
)

data class TalentTree(
    val characterId: CharacterId,
    val nodes: List<TalentNode>
)

object CharacterLibrary {
    fun get(characterId: CharacterId): Character {
        return when (characterId) {
            CharacterId.GUAN_YU -> Character(
                id = CharacterId.GUAN_YU,
                displayName = "Guan Yu",
                faction = Faction.SHU,
                passiveName = "War Saint",
                passiveDescription = "Increases damage of 'Slash' cards by 1.",
                activeSkill = Skill("Godly Strike", "Deal 2 damage to enemy.", 2),
                ultimateSkill = Skill("Dragon's Awakening", "Gain 2 Strength and draw 2 cards.", 4, true),
                maxHearts = 5,
                attack = 14,
                defense = 6,
                speed = 10,
                energy = 3,
                critChance = 0.20
            )
            CharacterId.ZHUGE_LIANG -> Character(
                id = CharacterId.ZHUGE_LIANG,
                displayName = "Zhuge Liang",
                faction = Faction.SHU,
                passiveName = "Master Strategist",
                passiveDescription = "Draws 1 extra card at start of turn.",
                activeSkill = Skill("Wind's Call", "Stun enemy for 1 turn.", 3),
                ultimateSkill = Skill("Empty Fort Strategy", "Gain 2 Block and reflect next damage.", 4, true),
                maxHearts = 4,
                attack = 9,
                defense = 7,
                speed = 12,
                energy = 3,
                critChance = 0.10
            )
            CharacterId.CAO_CAO -> Character(
                id = CharacterId.CAO_CAO,
                displayName = "Cao Cao",
                faction = Faction.WEI,
                passiveName = "Ruthless Command",
                passiveDescription = "Heal 1 HP whenever an enemy is defeated.",
                activeSkill = Skill("Imperial Decree", "Reduce enemy Strength by 1.", 2),
                ultimateSkill = Skill("King of Ambition", "Steal 1 card from enemy or gain Rage.", 4, true),
                maxHearts = 5,
                attack = 11,
                defense = 8,
                speed = 11,
                energy = 3,
                critChance = 0.12
            )
            CharacterId.ZHOU_YU -> Character(
                id = CharacterId.ZHOU_YU,
                displayName = "Zhou Yu",
                faction = Faction.WU,
                passiveName = "Crimson Inferno",
                passiveDescription = "Burn effects deal +1 damage.",
                activeSkill = Skill("Fanning the Flames", "Apply 2 Burn to enemy.", 2),
                ultimateSkill = Skill("Red Cliff Blaze", "Deal 1 damage to all enemies and apply 3 Burn.", 4, true),
                maxHearts = 4,
                attack = 10,
                defense = 6,
                speed = 13,
                energy = 3,
                critChance = 0.10
            )
            CharacterId.LU_BU -> Character(
                id = CharacterId.LU_BU,
                displayName = "Lu Bu",
                faction = Faction.OTHER,
                passiveName = "Peerless Berserker",
                passiveDescription = "Gain +1 Attack for each heart lost.",
                activeSkill = Skill("Crescent Slash", "Deal 1 damage and gain 1 Rage.", 1),
                ultimateSkill = Skill("Demon of the Battlefield", "Deal 4 damage, but lose 1 Heart.", 5, true),
                maxHearts = 3,
                attack = 16,
                defense = 4,
                speed = 15,
                energy = 3,
                critChance = 0.18
            )
        }
    }
}

object TalentTreeLibrary {
    fun treeFor(characterId: CharacterId): TalentTree {
        val nodes = when (characterId) {
            CharacterId.GUAN_YU -> listOf(
                TalentNode("gy_off_1", "Green Dragon Mastery", TalentPath.OFFENSE, "+20% attack.", 2, 0.20, rarity = TalentRarity.COMMON),
                TalentNode("gy_off_2", "War Saint Critical", TalentPath.OFFENSE, "Critical hits deal double damage.", 3, 1.0, listOf("gy_off_1"), rarity = TalentRarity.RARE),
                TalentNode("gy_off_3", "Execution Momentum", TalentPath.OFFENSE, "Gain +1 energy on kill.", 3, 1.0, listOf("gy_off_2"), rarity = TalentRarity.LEGENDARY),
                TalentNode("gy_def_1", "Iron Armor", TalentPath.DEFENSE, "+1 heart.", 2, 1.0),
                TalentNode("gy_def_2", "Battle Focus", TalentPath.DEFENSE, "Start battle with 1 block.", 2, 1.0, listOf("gy_def_1")),
                TalentNode("gy_def_3", "Unbreakable Will", TalentPath.DEFENSE, "Take 10% less damage.", 3, 0.10, listOf("gy_def_2"), rarity = TalentRarity.RARE),
                TalentNode("gy_utl_1", "Quick Formation", TalentPath.UTILITY, "Start with +1 energy.", 2, 1.0),
                TalentNode("gy_utl_2", "War Drums", TalentPath.UTILITY, "Draw +1 card per turn.", 3, 1.0, listOf("gy_utl_1")),
                TalentNode("gy_utl_3", "Commander Presence", TalentPath.UTILITY, "+10 gold on victory.", 2, 10.0, listOf("gy_utl_2")),
                TalentNode("gy_utl_4", "Blade of Loyalty", TalentPath.UTILITY, "First attack each turn gets +25% damage.", 3, 0.25, listOf("gy_utl_3"), rarity = TalentRarity.LEGENDARY)
            )
            CharacterId.ZHUGE_LIANG -> listOf(
                TalentNode("zl_off_1", "Calculated Strike", TalentPath.OFFENSE, "+15% attack.", 2, 0.15),
                TalentNode("zl_off_2", "Elemental Focus", TalentPath.OFFENSE, "Burn and strategy damage +25%.", 3, 0.25, listOf("zl_off_1")),
                TalentNode("zl_off_3", "Perfect Timing", TalentPath.OFFENSE, "+10% crit chance.", 2, 0.10, listOf("zl_off_2")),
                TalentNode("zl_def_1", "Field Barriers", TalentPath.DEFENSE, "+1 heart.", 2, 1.0),
                TalentNode("zl_def_2", "Woven Defense", TalentPath.DEFENSE, "Start with 1 block.", 2, 1.0, listOf("zl_def_1")),
                TalentNode("zl_def_3", "Counter Tactics", TalentPath.DEFENSE, "Enemy loses 1 strength every 3 turns.", 3, 1.0, listOf("zl_def_2")),
                TalentNode("zl_utl_1", "Foresight", TalentPath.UTILITY, "Draw +1 card each turn.", 2, 1.0),
                TalentNode("zl_utl_2", "Cost Prediction", TalentPath.UTILITY, "Reduce one card cost by 1 each turn.", 3, 1.0, listOf("zl_utl_1")),
                TalentNode("zl_utl_3", "Eight Trigrams", TalentPath.UTILITY, "Strategy effects +30%.", 3, 0.30, listOf("zl_utl_2"), rarity = TalentRarity.RARE),
                TalentNode("zl_utl_4", "Silent Command", TalentPath.UTILITY, "+1 energy every 3 turns.", 2, 1.0, listOf("zl_utl_3"))
            )
            CharacterId.CAO_CAO -> listOf(
                TalentNode("cc_off_1", "Ruthless Slash", TalentPath.OFFENSE, "+15% attack.", 2, 0.15),
                TalentNode("cc_off_2", "Imperial Pressure", TalentPath.OFFENSE, "Deal +20% to bosses.", 3, 0.20, listOf("cc_off_1"), rarity = TalentRarity.RARE),
                TalentNode("cc_off_3", "Warlord's Edge", TalentPath.OFFENSE, "+12% crit chance.", 2, 0.12, listOf("cc_off_2")),
                TalentNode("cc_def_1", "Fortified Court", TalentPath.DEFENSE, "+1 heart.", 2, 1.0),
                TalentNode("cc_def_2", "Dark Resilience", TalentPath.DEFENSE, "Lifesteal heals +1.", 3, 1.0, listOf("cc_def_1")),
                TalentNode("cc_def_3", "Iron Decree", TalentPath.DEFENSE, "Take 12% less damage.", 3, 0.12, listOf("cc_def_2")),
                TalentNode("cc_utl_1", "Command Network", TalentPath.UTILITY, "Start with +1 energy.", 2, 1.0),
                TalentNode("cc_utl_2", "War Tax", TalentPath.UTILITY, "+15 gold on victory.", 2, 15.0, listOf("cc_utl_1")),
                TalentNode("cc_utl_3", "Calculated Cruelty", TalentPath.UTILITY, "Drain cards cost -1 once per turn.", 3, 1.0, listOf("cc_utl_2")),
                TalentNode("cc_utl_4", "Northern Ambition", TalentPath.UTILITY, "At half hearts, gain +20% damage.", 3, 0.20, listOf("cc_utl_3"))
            )
            CharacterId.ZHOU_YU -> listOf(
                TalentNode("zy_off_1", "Flame Command", TalentPath.OFFENSE, "+20% fire damage.", 2, 0.20),
                TalentNode("zy_off_2", "Sea of Fire", TalentPath.OFFENSE, "Burn stacks +1.", 3, 1.0, listOf("zy_off_1")),
                TalentNode("zy_off_3", "Scorched Sky", TalentPath.OFFENSE, "Enemies with burn take +25% damage.", 3, 0.25, listOf("zy_off_2"), rarity = TalentRarity.RARE),
                TalentNode("zy_def_1", "Naval Shield", TalentPath.DEFENSE, "+1 heart.", 2, 1.0),
                TalentNode("zy_def_2", "Wave Guard", TalentPath.DEFENSE, "Start with 1 block.", 2, 1.0, listOf("zy_def_1")),
                TalentNode("zy_def_3", "Heat Screen", TalentPath.DEFENSE, "Take 10% less damage from elites/bosses.", 3, 0.10, listOf("zy_def_2")),
                TalentNode("zy_utl_1", "Battle Rhythm", TalentPath.UTILITY, "Draw +1 card every other turn.", 2, 1.0),
                TalentNode("zy_utl_2", "Fleet Maneuver", TalentPath.UTILITY, "First strategy card each turn costs -1.", 3, 1.0, listOf("zy_utl_1")),
                TalentNode("zy_utl_3", "Red Cliff Doctrine", TalentPath.UTILITY, "Strategy effects +25%.", 3, 0.25, listOf("zy_utl_2")),
                TalentNode("zy_utl_4", "Pyre Tribute", TalentPath.UTILITY, "+12 gold on victory.", 2, 12.0, listOf("zy_utl_3"))
            )
            CharacterId.LU_BU -> listOf(
                TalentNode("lb_off_1", "Unmatched Force", TalentPath.OFFENSE, "+25% attack.", 3, 0.25, rarity = TalentRarity.RARE),
                TalentNode("lb_off_2", "Blood Frenzy", TalentPath.OFFENSE, "Below 50% HP: +50% damage.", 3, 0.50, listOf("lb_off_1"), rarity = TalentRarity.LEGENDARY),
                TalentNode("lb_off_3", "Demonic Surge", TalentPath.OFFENSE, "Below 30% HP: +100% damage.", 4, 1.00, listOf("lb_off_2"), rarity = TalentRarity.LEGENDARY),
                TalentNode("lb_def_1", "Scarred Veteran", TalentPath.DEFENSE, "+1 heart.", 2, 1.0),
                TalentNode("lb_def_2", "Pain Tolerance", TalentPath.DEFENSE, "Take 8% less damage.", 2, 0.08, listOf("lb_def_1")),
                TalentNode("lb_def_3", "Reckless Guard", TalentPath.DEFENSE, "Gain 1 block when dropping below 50% HP.", 3, 1.0, listOf("lb_def_2")),
                TalentNode("lb_utl_1", "Tyrant Charge", TalentPath.UTILITY, "Start with +1 energy.", 2, 1.0),
                TalentNode("lb_utl_2", "Warpath", TalentPath.UTILITY, "Gain +1 energy on kill.", 3, 1.0, listOf("lb_utl_1")),
                TalentNode("lb_utl_3", "Cruel Tribute", TalentPath.UTILITY, "+20 gold on victory.", 2, 20.0, listOf("lb_utl_2")),
                TalentNode("lb_utl_4", "Fatal Balance", TalentPath.UTILITY, "Lose 1 heart at battle start, gain +20% damage.", 2, 0.20, listOf("lb_utl_3"), rarity = TalentRarity.RARE)
            )
        }
        return TalentTree(characterId, nodes)
    }
}
