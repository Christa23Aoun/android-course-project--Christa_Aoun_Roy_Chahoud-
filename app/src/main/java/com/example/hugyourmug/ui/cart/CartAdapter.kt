package com.example.hugyourmug.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.R
import com.example.hugyourmug.data.model.CartItem

class CartAdapter(
    private val onIncrease: (CartItem) -> Unit,
    private val onDecrease: (CartItem) -> Unit,
    private val onDelete: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var items: List<CartItem> = emptyList()

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtCartItemName)
        val txtPrice: TextView = itemView.findViewById(R.id.txtCartItemPrice)
        val txtQuantity: TextView = itemView.findViewById(R.id.txtCartQuantity)
        val btnIncrease: ImageView = itemView.findViewById(R.id.btnIncrease)
        val btnDecrease: ImageView = itemView.findViewById(R.id.btnDecrease)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]

        holder.txtName.text = item.name
        holder.txtPrice.text = "$${item.price}"
        holder.txtQuantity.text = item.quantity.toString()

        holder.btnIncrease.setOnClickListener { onIncrease(item) }
        holder.btnDecrease.setOnClickListener { onDecrease(item) }
        holder.btnDelete.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
