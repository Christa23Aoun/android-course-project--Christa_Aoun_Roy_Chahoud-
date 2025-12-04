package com.example.hugyourmug.ui.checkout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.R
import com.example.hugyourmug.data.CartItem

class CheckoutItemsAdapter(
    private val items: List<CartItem>
) : RecyclerView.Adapter<CheckoutItemsAdapter.CheckoutViewHolder>() {

    inner class CheckoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgCheckoutCoffee)
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

        holder.img.setImageResource(item.imageResId)
        holder.txtName.text = item.name
        holder.txtQty.text = "x${item.quantity}"
        holder.txtPrice.text = "$${item.price * item.quantity}"
    }

    override fun getItemCount(): Int = items.size
}
