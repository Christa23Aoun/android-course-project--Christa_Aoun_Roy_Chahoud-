package com.example.hugyourmug.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.R
import com.example.hugyourmug.data.model.Order
import java.text.SimpleDateFormat
import java.util.*

class OrderHistoryAdapter(
    private val onClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>() {

    private var orders: List<Order> = emptyList()

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDate: TextView = itemView.findViewById(R.id.txtOrderDate)
        val txtType: TextView = itemView.findViewById(R.id.txtOrderType)
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
        holder.txtDate.text = sdf.format(Date(order.timestamp))

        holder.txtType.text =
            if (order.isDelivery)
                "Delivery • Bring change: ${if (order.bringChange) "Yes" else "No"}"
            else "Pickup"

        holder.txtTotal.text = "$${String.format("%.2f", order.total)}"

        holder.itemView.setOnClickListener { onClick(order) }
    }

    override fun getItemCount() = orders.size

    fun updateList(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}
