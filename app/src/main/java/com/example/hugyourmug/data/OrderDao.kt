package com.example.hugyourmug.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {

    @Insert
    suspend fun insert(order: Order): Long

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getOrdersForUser(userId: Int): List<Order>

    // This is the method to get a specific order by its ID.
    @Query("SELECT * FROM orders WHERE orderId = :id")
    suspend fun getOrderById(id: Int): Order
}
