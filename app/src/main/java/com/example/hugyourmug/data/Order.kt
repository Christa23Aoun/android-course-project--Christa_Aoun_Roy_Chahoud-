package com.example.hugyourmug.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val fullName: String,
    val address: String,
    val isDelivery: Boolean,
    val bringChange: Boolean,
    val total: Double,
    val timestamp: Long
)
