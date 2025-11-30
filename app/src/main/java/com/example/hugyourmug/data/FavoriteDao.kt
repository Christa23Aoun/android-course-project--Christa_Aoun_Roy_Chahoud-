package com.example.hugyourmug.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteItem: FavoriteItem)

    @Delete
    suspend fun delete(favoriteItem: FavoriteItem)

    @Query("SELECT * FROM favorite_items WHERE userId = :userId")
    suspend fun getFavoritesForUser(userId: Int): List<FavoriteItem>

    @Query("SELECT * FROM favorite_items WHERE userId = :userId AND name = :name LIMIT 1")
    suspend fun getFavoriteForUserAndName(userId: Int, name: String): FavoriteItem?
}
