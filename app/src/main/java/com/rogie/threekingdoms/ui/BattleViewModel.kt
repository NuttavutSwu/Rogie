package com.rogie.threekingdoms.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rogie.threekingdoms.game.BattleManager
import com.rogie.threekingdoms.game.DeckManager
import com.rogie.threekingdoms.game.GameSession
import com.rogie.threekingdoms.model.Card
import com.rogie.threekingdoms.model.Enemy

class BattleViewModel : ViewModel() {
    private lateinit var battleManager: BattleManager
    private lateinit var deckManager: DeckManager
    private lateinit var enemy: Enemy

    private val _hand = MutableLiveData<List<Card>>()
    val hand: LiveData<List<Card>> = _hand

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    private val _battleEnded = MutableLiveData<Boolean>(false)
    val battleEnded: LiveData<Boolean> = _battleEnded

    fun setupBattle() {
        val player = GameSession.player
        player.baseEnergy = if (GameSession.hasImperialSeal) 4 else 3
        enemy = GameSession.buildEnemy()
        deckManager = DeckManager(GameSession.deck)
        battleManager = BattleManager(player, enemy, deckManager)
        battleManager.startBattle()
        _hand.value = deckManager.hand.toList()
        _status.value = "Encounter: ${enemy.name}"
        _battleEnded.value = false
    }

    fun playCard(card: Card) {
        val result = battleManager.playerPlayCard(card)
        if (enemy.hp <= 0) {
            GameSession.player.gold += 20 + GameSession.bonusGoldPerVictory
            GameSession.onEnemyDefeated(enemy)
            _status.value = "$result Victory! +20 gold."
            _battleEnded.value = true
        } else {
            _status.value = result
        }
        _hand.value = deckManager.hand.toList()
    }

    fun endTurn() {
        val result = battleManager.endPlayerTurn()
        if (enemy.hp <= 0) {
            GameSession.player.gold += 20 + GameSession.bonusGoldPerVictory
            GameSession.onEnemyDefeated(enemy)
            _status.value = "Victory! +20 gold."
            _battleEnded.value = true
        } else if (GameSession.player.hp <= 0) {
            _status.value = "Defeated. Your war campaign has ended."
            _battleEnded.value = true
        } else {
            _status.value = result
        }
        _hand.value = deckManager.hand.toList()
    }

    fun playerHpText(): String = "${GameSession.player.hp}/${GameSession.player.maxHp}"
    fun playerNumericHpText(): String = "${GameSession.player.hp}/${GameSession.player.maxHp}"
    fun playerEnergyText(): String = GameSession.player.energy.toString()
    fun playerGoldText(): String = GameSession.player.gold.toString()
    fun enemyHpText(): String = "${enemy.hp}/${enemy.maxHp}"
    fun enemyNumericHpText(): String = "${enemy.hp}/${enemy.maxHp}"
    fun enemyNameText(): String = enemy.name
    fun characterStatsText(): String {
        val c = GameSession.character
        return "ATK ${c.attack} | DEF ${c.defense} | Crit ${(GameSession.critChance * 100).toInt()}%"
    }

    fun playerHpValue(): Int = GameSession.player.hp
    fun playerMaxHpValue(): Int = GameSession.player.maxHp
    fun enemyHpValue(): Int = enemy.hp
    fun enemyMaxHpValue(): Int = enemy.maxHp
}
