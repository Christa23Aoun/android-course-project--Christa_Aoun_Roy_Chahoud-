package com.example.hugyourmug.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItem)

    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    suspend fun getCartItemsForUser(userId: Int): List<CartItem>

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND name = :name LIMIT 1")
    suspend fun getCartItemForUserAndName(userId: Int, name: String): CartItem?

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Int, quantity: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteItemById(id: Int)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCartForUser(userId: Int)
}
