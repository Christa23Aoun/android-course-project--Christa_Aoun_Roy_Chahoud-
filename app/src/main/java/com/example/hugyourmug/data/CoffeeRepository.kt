package com.example.hugyourmug.data

import androidx.lifecycle.LiveData

class CoffeeRepository(private val coffeeDao: CoffeeDao) {

    // Get all coffees
    val allCoffees: LiveData<List<Coffee>> = coffeeDao.getAllCoffees()

    // Insert a new coffee
    suspend fun insert(coffee: Coffee) {
        coffeeDao.insertCoffee(coffee)
    }

    // Delete one coffee
    suspend fun delete(coffee: Coffee) {
        coffeeDao.deleteCoffee(coffee)
    }

    // Delete all coffees
    suspend fun deleteAll() {
        coffeeDao.deleteAllCoffees()
    }
}
