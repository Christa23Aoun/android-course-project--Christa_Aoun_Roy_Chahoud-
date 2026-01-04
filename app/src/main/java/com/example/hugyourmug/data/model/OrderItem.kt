package com.example.hugyourmug.data.model

data class OrderItem(
    val id: String = "",
    val orderId: String = "",
    val coffeeId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1
)
