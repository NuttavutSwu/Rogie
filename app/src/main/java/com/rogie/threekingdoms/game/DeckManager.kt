package com.rogie.threekingdoms.game

import com.rogie.threekingdoms.model.Card
import kotlin.random.Random

class DeckManager(cards: List<Card>) {
    private val drawPile = cards.shuffled(Random(System.currentTimeMillis())).toMutableList()
    private val discardPile = mutableListOf<Card>()
    val hand = mutableListOf<Card>()

    fun drawCards(amount: Int) {
        repeat(amount) {
            if (drawPile.isEmpty()) {
                if (discardPile.isEmpty()) return
                drawPile.addAll(discardPile.shuffled())
                discardPile.clear()
            }
            hand.add(drawPile.removeAt(0))
        }
    }

    fun discardHand() {
        discardPile.addAll(hand)
        hand.clear()
    }

    fun removeFromHand(card: Card) {
        hand.remove(card)
        discardPile.add(card)
    }
}
