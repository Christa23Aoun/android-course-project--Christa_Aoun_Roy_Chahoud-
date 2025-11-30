package com.example.hugyourmug.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_items")
data class FavoriteItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val name: String,
    val price: Double,
    val imageResId: Int
)
