package com.example.hugyourmug.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.R
import com.example.hugyourmug.data.Order
import java.text.SimpleDateFormat
import java.util.*

class OrderHistoryAdapter(
    private val orders: List<Order>,
    private val onClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDate: TextView = itemView.findViewById(R.id.txtOrderDate)
        val txtType: TextView = itemView.findViewById(R.id.txtOrderType)
        val txtItems: TextView = itemView.findViewById(R.id.txtOrderItems)
        val txtTotal: TextView = itemView.findViewById(R.id.txtOrderTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_history, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        val sdf = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
        val dateText = sdf.format(Date(order.timestamp))

        holder.txtDate.text = dateText
        holder.txtType.text = if (order.isDelivery)
            "Delivery • Bring change: ${if (order.bringChange) "Yes" else "No"}"
        else "Pickup"

        holder.txtItems.text = "${orderItemsCount(order.orderId)} items"
        holder.txtTotal.text = "$${String.format("%.2f", order.total)}"

        holder.itemView.setOnClickListener { onClick(order) }
    }

    override fun getItemCount() = orders.size

    private fun orderItemsCount(orderId: Int): Int {
        return 0 // Will update in Step 4
    }
}
