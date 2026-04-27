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
    OFFENSE, DEFENSE, UTILITY, UNIQUE_CARD
}

enum class TalentRarity {
    COMMON, RARE, EPIC, LEGENDARY
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
                TalentNode("gy_unique_1", "Unlock: Green Dragon Blade", TalentPath.UNIQUE_CARD, "Unlock the legendary blade card.", 10, rarity = TalentRarity.LEGENDARY),
                TalentNode("gy_def_1", "Iron Armor", TalentPath.DEFENSE, "+1 heart.", 2, 1.0),
                TalentNode("gy_def_2", "Battle Focus", TalentPath.DEFENSE, "Start battle with 1 block.", 2, 1.0, listOf("gy_def_1")),
                TalentNode("gy_utl_1", "Quick Formation", TalentPath.UTILITY, "Start with +1 energy.", 2, 1.0)
            )
            CharacterId.ZHUGE_LIANG -> listOf(
                TalentNode("zl_off_1", "Calculated Strike", TalentPath.OFFENSE, "+15% attack.", 2, 0.15),
                TalentNode("zl_unique_1", "Unlock: Prayer to Stars", TalentPath.UNIQUE_CARD, "Unlock the ultimate strategy card.", 10, rarity = TalentRarity.LEGENDARY),
                TalentNode("zl_def_1", "Field Barriers", TalentPath.DEFENSE, "+1 heart.", 2, 1.0),
                TalentNode("zl_utl_1", "Foresight", TalentPath.UTILITY, "Draw +1 card each turn.", 2, 1.0)
            )
            CharacterId.CAO_CAO -> listOf(
                TalentNode("cc_off_1", "Ruthless Slash", TalentPath.OFFENSE, "+15% attack.", 2, 0.15),
                TalentNode("cc_unique_1", "Unlock: Imperial Decree", TalentPath.UNIQUE_CARD, "Unlock the supreme command card.", 10, rarity = TalentRarity.LEGENDARY),
                TalentNode("cc_def_1", "Fortified Court", TalentPath.DEFENSE, "+1 heart.", 2, 1.0),
                TalentNode("cc_utl_1", "Command Network", TalentPath.UTILITY, "Start with +1 energy.", 2, 1.0)
            )
            CharacterId.ZHOU_YU -> listOf(
                TalentNode("zy_off_1", "Flame Command", TalentPath.OFFENSE, "+20% fire damage.", 2, 0.20),
                TalentNode("zy_unique_1", "Unlock: Crimson Inferno", TalentPath.UNIQUE_CARD, "Unlock the massive burn card.", 10, rarity = TalentRarity.LEGENDARY),
                TalentNode("zy_def_1", "Naval Shield", TalentPath.DEFENSE, "+1 heart.", 2, 1.0),
                TalentNode("zy_utl_1", "Battle Rhythm", TalentPath.UTILITY, "Draw +1 card every other turn.", 2, 1.0)
            )
            CharacterId.LU_BU -> listOf(
                // Lu Bu Core Stats
                TalentNode("lb_off_1", "Unmatched Force", TalentPath.OFFENSE, "+25% attack.", 3, 0.25, rarity = TalentRarity.RARE),
                TalentNode("lb_def_1", "Scarred Veteran", TalentPath.DEFENSE, "+1 heart.", 2, 1.0),
                TalentNode("lb_utl_1", "Tyrant Charge", TalentPath.UTILITY, "Start with +1 energy.", 2, 1.0),

                // Lu Bu Advanced Card Set Unlocks
                TalentNode("lb_card_fever", "Unlock: Battle Fever", TalentPath.UNIQUE_CARD, "Gain Rage when playing cards.", 4, rarity = TalentRarity.COMMON),
                TalentNode("lb_card_halberd", "Unlock: Crushing Halberd", TalentPath.UNIQUE_CARD, "Powerful attack with Stun potential.", 5, requires = listOf("lb_card_fever"), rarity = TalentRarity.RARE),
                TalentNode("lb_card_overdrive", "Unlock: Overdrive Rage", TalentPath.UNIQUE_CARD, "Double Rage for a massive HP cost.", 6, requires = listOf("lb_card_halberd"), rarity = TalentRarity.EPIC),
                
                TalentNode("lb_card_execute", "Unlock: Execution Slash", TalentPath.UNIQUE_CARD, "Instantly kill weak targets.", 5, rarity = TalentRarity.RARE),
                TalentNode("lb_card_falling", "Unlock: Falling Sky Strike", TalentPath.UNIQUE_CARD, "Massive AoE damage.", 7, requires = listOf("lb_card_execute"), rarity = TalentRarity.EPIC),
                
                TalentNode("lb_card_breath", "Unlock: Last Breath", TalentPath.UNIQUE_CARD, "Damage boost when near death.", 5, rarity = TalentRarity.RARE),
                TalentNode("lb_card_explode", "Unlock: Blood Explosion", TalentPath.UNIQUE_CARD, "Multi-hit strike at HP cost.", 6, requires = listOf("lb_card_breath"), rarity = TalentRarity.RARE),
                
                TalentNode("lb_card_mirror", "Unlock: Pain Mirror", TalentPath.UNIQUE_CARD, "Reflect incoming damage.", 5, rarity = TalentRarity.RARE),
                TalentNode("lb_card_recovery", "Unlock: War Recovery", TalentPath.UNIQUE_CARD, "Heal using your Rage.", 6, requires = listOf("lb_card_mirror"), rarity = TalentRarity.RARE),
                
                TalentNode("lb_card_iron", "Unlock: Iron Body", TalentPath.UNIQUE_CARD, "Ultimate defense at the cost of attack.", 5, rarity = TalentRarity.RARE),
                TalentNode("lb_card_chain", "Unlock: Chain Assault", TalentPath.UNIQUE_CARD, "Repeating attack combo.", 6, requires = listOf("lb_card_iron"), rarity = TalentRarity.RARE),
                
                TalentNode("lb_card_fury", "Unlock: Relentless Fury", TalentPath.UNIQUE_CARD, "Free cards on kills.", 8, requires = listOf("lb_card_chain"), rarity = TalentRarity.EPIC),
                TalentNode("lb_card_momentum", "Unlock: War Momentum", TalentPath.UNIQUE_CARD, "Energy reduction combo.", 6, rarity = TalentRarity.RARE),
                TalentNode("lb_card_instinct", "Unlock: Demonic Instinct", TalentPath.UNIQUE_CARD, "Guaranteed criticals at low HP.", 7, requires = listOf("lb_card_momentum"), rarity = TalentRarity.RARE),
                
                TalentNode("lb_card_transform", "Unlock: Rage Incarnate", TalentPath.UNIQUE_CARD, "Lü Bu's ultimate transformation.", 15, requires = listOf("lb_card_overdrive", "lb_card_falling", "lb_card_instinct"), rarity = TalentRarity.LEGENDARY)
            )
        }
        return TalentTree(characterId, nodes)
    }
}
