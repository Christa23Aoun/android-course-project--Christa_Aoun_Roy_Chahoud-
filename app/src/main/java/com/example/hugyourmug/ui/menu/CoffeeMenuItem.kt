package com.example.hugyourmug.ui.menu

data class CoffeeMenuItem(
    val name: String,
    val price: Double,
    val oldPrice: Double,
    val imageResId: Int,
    val moodTag: String   // will be used later for mood-based recommendation
)
