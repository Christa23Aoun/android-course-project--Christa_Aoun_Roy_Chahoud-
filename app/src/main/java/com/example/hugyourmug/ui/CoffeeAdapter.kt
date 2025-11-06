package com.example.hugyourmug.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.R
import com.example.hugyourmug.data.Coffee

class CoffeeAdapter(
    private var coffees: List<Coffee>,
    private val onItemClick: (Coffee) -> Unit
) : RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder>() {

    inner class CoffeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        val txtDescription: TextView = itemView.findViewById(R.id.txtDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coffee, parent, false)
        return CoffeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val coffee = coffees[position]
        holder.txtName.text = coffee.name
        holder.txtPrice.text = "$${coffee.price}"
        holder.txtDescription.text = coffee.description

        holder.itemView.setOnClickListener { onItemClick(coffee) }
    }

    override fun getItemCount() = coffees.size

    fun updateData(newList: List<Coffee>) {
        coffees = newList
        notifyDataSetChanged()
    }
    fun getItemAt(position: Int): Coffee = coffees[position]

}
