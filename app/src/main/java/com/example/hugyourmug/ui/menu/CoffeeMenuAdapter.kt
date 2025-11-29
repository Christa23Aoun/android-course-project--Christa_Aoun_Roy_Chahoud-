package com.example.hugyourmug.ui.menu

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.R

class CoffeeMenuAdapter(
    private val items: List<CoffeeMenuItem>,
    private val onAddToCartClick: (CoffeeMenuItem) -> Unit
) : RecyclerView.Adapter<CoffeeMenuAdapter.CoffeeViewHolder>() {

    inner class CoffeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCoffee: ImageView = itemView.findViewById(R.id.imgCoffee)
        val txtName: TextView = itemView.findViewById(R.id.txtCoffeeName)
        val txtPrice: TextView = itemView.findViewById(R.id.txtCoffeePrice)
        val txtOldPrice: TextView = itemView.findViewById(R.id.txtCoffeeOldPrice)
        val btnAddToCart: Button = itemView.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu_coffee, parent, false)
        return CoffeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val item = items[position]
        holder.imgCoffee.setImageResource(item.imageResId)
        holder.txtName.text = item.name
        holder.txtPrice.text = "${item.price}$"
        holder.txtOldPrice.text = "${item.oldPrice}$"
        holder.txtOldPrice.paintFlags =
            holder.txtOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.btnAddToCart.setOnClickListener { onAddToCartClick(item) }
    }

    override fun getItemCount(): Int = items.size
}
