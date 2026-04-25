package com.rogie.threekingdoms.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.model.Card

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
        fun bind(card: Card, onCardClicked: (Card) -> Unit) {
            itemView.findViewById<TextView>(R.id.tvCardName).text = card.name
            itemView.findViewById<TextView>(R.id.tvCardCost).text = "Cost ${card.energyCost}"
            itemView.findViewById<TextView>(R.id.tvCardType).text = card.type.name
            itemView.findViewById<TextView>(R.id.tvCardDesc).text = card.description
            itemView.setOnClickListener { onCardClicked(card) }
        }
    }
}
