package com.example.hugyourmug.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val orderId: Int,     // FK → Order.id
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageResId: Int
)
