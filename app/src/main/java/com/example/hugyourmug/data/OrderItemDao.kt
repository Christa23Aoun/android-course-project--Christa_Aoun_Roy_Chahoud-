package com.example.hugyourmug.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderItemDao {

    @Insert
    suspend fun insert(item: OrderItem)

    @Insert
    suspend fun insertAll(items: List<OrderItem>)

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getItemsForOrder(orderId: Int): List<OrderItem>
}
