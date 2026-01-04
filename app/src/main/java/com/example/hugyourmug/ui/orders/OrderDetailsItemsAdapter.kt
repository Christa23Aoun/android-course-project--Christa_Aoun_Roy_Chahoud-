package com.example.hugyourmug.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.R
import com.example.hugyourmug.data.model.OrderItem

class OrderDetailsItemsAdapter :
    RecyclerView.Adapter<OrderDetailsItemsAdapter.ItemViewHolder>() {

    private var items: List<OrderItem> = emptyList()

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtItemName)
        val txtPrice: TextView = itemView.findViewById(R.id.txtItemPrice)
        val txtQuantity: TextView = itemView.findViewById(R.id.txtItemQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_detail, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        holder.txtName.text = item.name
        holder.txtPrice.text = "$%.2f".format(item.price)
        holder.txtQuantity.text = "x${item.quantity}"
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<OrderItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
