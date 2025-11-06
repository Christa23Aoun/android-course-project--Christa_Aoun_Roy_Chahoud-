package com.example.hugyourmug.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CoffeeDao {

    // Return all coffees
    @Query("SELECT * FROM coffee_table ORDER BY id DESC")
    fun getAllCoffees(): LiveData<List<Coffee>>

    // Insert a coffee
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoffee(coffee: Coffee)

    // Delete one coffee
    @Delete
    suspend fun deleteCoffee(coffee: Coffee)

    // Delete all coffees
    @Query("DELETE FROM coffee_table")
    suspend fun deleteAllCoffees()
}
