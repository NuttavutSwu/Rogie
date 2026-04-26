package com.rogie.threekingdoms.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.model.Card
import com.rogie.threekingdoms.model.CardType
import com.rogie.threekingdoms.game.GameSession

class CardAdapter(
    private val onCardClicked: (Card) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    private val cards = mutableListOf<Card>()

    fun update(newCards: List<Card>) {
        cards.clear()
        cards.addAll(newCards)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int = cards.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position], onCardClicked)
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNameHeader = itemView.findViewById<TextView>(R.id.tvCardNameHeader)
        private val tvNameLabel = itemView.findViewById<TextView>(R.id.tvNameLabel)
        private val tvTypeLabel = itemView.findViewById<TextView>(R.id.tvTypeLabel)
        private val tvCostTop = itemView.findViewById<TextView>(R.id.tvCardCostTop)
        private val tvFaction = itemView.findViewById<TextView>(R.id.tvFactionSymbol)
        private val tvDesc = itemView.findViewById<TextView>(R.id.tvCardDesc)
        private val tvAtk = itemView.findViewById<TextView>(R.id.tvStatAtk)
        private val tvDef = itemView.findViewById<TextView>(R.id.tvStatDef)
        private val tvCostBottom = itemView.findViewById<TextView>(R.id.tvStatCost)

        fun bind(card: Card, onCardClicked: (Card) -> Unit) {
            tvNameHeader.text = card.name
            tvNameLabel.text = "NAME: ${card.name}"
            tvTypeLabel.text = "TYPE: ${card.type.name}"
            tvCostTop.text = card.energyCost.toString()
            tvDesc.text = card.description
            tvCostBottom.text = "COST: ${card.energyCost}"
            
            tvAtk.text = "STRENGTH: ${if (card.type == CardType.ATTACK) card.value else "-"}"
            tvDef.text = "DEFENSE: ${if (card.type == CardType.DEFENSE) card.value else "-"}"

            tvFaction.text = when(GameSession.player.faction.name) {
                "SHU" -> "蜀"
                "WEI" -> "魏"
                "WU" -> "吳"
                else -> "漢"
            }

            itemView.setOnClickListener { onCardClicked(card) }
        }
    }
}
