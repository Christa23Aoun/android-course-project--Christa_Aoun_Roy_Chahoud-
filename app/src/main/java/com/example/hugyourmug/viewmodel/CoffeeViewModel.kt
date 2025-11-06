package com.example.hugyourmug.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.hugyourmug.data.AppDatabase
import com.example.hugyourmug.data.Coffee
import com.example.hugyourmug.data.CoffeeRepository
import kotlinx.coroutines.launch

class CoffeeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CoffeeRepository
    val allCoffees: LiveData<List<Coffee>>

    init {
        val coffeeDao = AppDatabase.getDatabase(application).coffeeDao()
        repository = CoffeeRepository(coffeeDao)
        allCoffees = repository.allCoffees
    }

    fun addCoffee(coffee: Coffee) = viewModelScope.launch {
        repository.insert(coffee)
    }

    fun deleteCoffee(coffee: Coffee) = viewModelScope.launch {
        repository.delete(coffee)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}
