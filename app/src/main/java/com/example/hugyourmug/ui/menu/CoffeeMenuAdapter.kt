package com.example.hugyourmug.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.R
import com.example.hugyourmug.data.model.Coffee

class CoffeeMenuAdapter(
    private var items: List<Coffee>,
    private val onAddClick: (Coffee) -> Unit,
    private val onDeleteClick: (Coffee) -> Unit
) : RecyclerView.Adapter<CoffeeMenuAdapter.CoffeeViewHolder>() {

    inner class CoffeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtCoffeeName)
        val txtPrice: TextView = itemView.findViewById(R.id.txtCoffeePrice)
        val btnAdd: Button = itemView.findViewById(R.id.btnAddToCart)
        val btnDelete: Button = itemView.findViewById(R.id.btnAddToFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu_coffee, parent, false)
        return CoffeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val coffee = items[position]

        holder.txtName.text = coffee.name
        holder.txtPrice.text = "${coffee.price}$"

        holder.btnAdd.setOnClickListener {
            onAddClick(coffee)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(coffee)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Coffee>) {
        items = newItems
        notifyDataSetChanged()
    }
}
