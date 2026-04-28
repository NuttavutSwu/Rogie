package com.rogie.threekingdoms.game

import com.rogie.threekingdoms.model.Card
import com.rogie.threekingdoms.model.EffectType
import com.rogie.threekingdoms.model.Player
import com.rogie.threekingdoms.model.Relic
import kotlin.random.Random

data class ShopOption(
    val text: String,
    val costGold: Int = 0,
    val costHp: Int = 0,
    val rewardPreview: String,
    val dialogue: String,
    val onSelect: (Player) -> Unit
)

data class MerchantEvent(
    val id: String,
    val name: String,
    val entryDialogue: String,
    val options: List<ShopOption>
)

object ShopLibrary {
    var shopEventSpawned = false

    fun resetChapterShop() {
        shopEventSpawned = false
    }

    fun getMerchantEvent(): MerchantEvent {
        val isSuspicious = Random.nextInt(100) < 20
        val priceMultiplier = if (isSuspicious) 0.7 else 1.0
        
        val options = mutableListOf<ShopOption>()

        // Option 1: Buy Card
        options.add(ShopOption(
            "Buy Card",
            (30 * priceMultiplier).toInt(),
            0,
            "1 Random Card",
            "Choose wisely. Power always comes with a price.",
            { player ->
                val pool = CardLibrary.rewardPool()
                GameSession.addCard(pool.random())
            }
        ))

        // Option 2: Buy Relic
        options.add(ShopOption(
            "Buy Relic",
            (80 * priceMultiplier).toInt(),
            0,
            "1 Random Relic",
            "This one... is quite special.",
            { player ->
                val relic = RelicLibrary.getRandomRelic()
                player.relics.add(relic)
                // Effects are applied via BattleManager or passive checks
            }
        ))

        // Option 3: Trade Health
        options.add(ShopOption(
            "Trade Health",
            0,
            (GameSession.player.maxHp * 0.15).toInt(),
            "Powerful Card",
            "A fair trade... your life for power.",
            { player ->
                val rarePool = CardLibrary.rewardPool().filter { it.rarity != com.rogie.threekingdoms.model.CardRarity.COMMON }
                GameSession.addCard(rarePool.random())
            }
        ))

        // Option 4: Remove Card
        options.add(ShopOption(
            "Remove Card",
            (50 * priceMultiplier).toInt(),
            0,
            "Remove 1 Card",
            "Sometimes... less is more.",
            { player ->
                if (GameSession.deck.isNotEmpty()) {
                    GameSession.deck.removeAt(Random.nextInt(GameSession.deck.size))
                }
            }
        ))

        // Option 5: Gamble
        options.add(ShopOption(
            "Gamble",
            20,
            0,
            "50% Win/Loss",
            "Fortune favors the bold... or the foolish.",
            { player ->
                if (Random.nextBoolean()) {
                    player.gold += 50
                } else {
                    player.hp = maxOf(1, player.hp - 5)
                }
            }
        ))

        if (isSuspicious) {
            options.add(ShopOption(
                "Take Mysterious Box",
                0,
                0,
                "???",
                "Trust me... or don't.",
                { player ->
                    if (Random.nextBoolean()) {
                        player.gold += 100
                    } else {
                        GameSession.chaosLevel += 3
                        player.maxHp -= 2
                        player.hp = minOf(player.hp, player.maxHp)
                    }
                }
            ))
        }

        options.add(ShopOption("Leave", 0, 0, "Nothing", "Another time, perhaps.", {}))

        return MerchantEvent(
            if (isSuspicious) "SUSPICIOUS_MERCHANT" else "WANDERING_MERCHANT",
            if (isSuspicious) "Suspicious Merchant" else "Wandering Merchant",
            if (isSuspicious) "The air feels heavy... This man's smile is too wide." else "A lone merchant stands amidst the battlefield, smiling calmly.",
            options.take(6)
        )
    }
}
