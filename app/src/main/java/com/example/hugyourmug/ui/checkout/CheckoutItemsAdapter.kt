package com.example.hugyourmug.ui.checkout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.R
import com.example.hugyourmug.data.model.CartItem

class CheckoutItemsAdapter :
    RecyclerView.Adapter<CheckoutItemsAdapter.CheckoutViewHolder>() {

    private var items: List<CartItem> = emptyList()

    inner class CheckoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtCheckoutName)
        val txtQty: TextView = itemView.findViewById(R.id.txtCheckoutQty)
        val txtPrice: TextView = itemView.findViewById(R.id.txtCheckoutPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkout_summary, parent, false)
        return CheckoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        val item = items[position]

        holder.txtName.text = item.name
        holder.txtQty.text = "x${item.quantity}"
        holder.txtPrice.text = "$%.2f".format(item.price * item.quantity)
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
