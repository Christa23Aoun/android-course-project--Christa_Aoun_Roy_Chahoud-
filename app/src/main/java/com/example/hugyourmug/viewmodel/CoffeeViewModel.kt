package com.example.hugyourmug.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hugyourmug.data.model.Coffee
import com.example.hugyourmug.data.repository.CoffeeRepository
import kotlinx.coroutines.launch

class CoffeeViewModel : ViewModel() {

    private val repository = CoffeeRepository()

    private val _allCoffees = MutableLiveData<List<Coffee>>()
    val allCoffees: LiveData<List<Coffee>> = _allCoffees

    init {
        loadCoffees()
    }

    private fun loadCoffees() {
        viewModelScope.launch {
            _allCoffees.value = repository.getAllCoffees()
        }
    }

    fun addCoffee(coffee: Coffee) {
        viewModelScope.launch {
            repository.addCoffee(coffee)
            loadCoffees()
        }
    }

    fun deleteCoffee(coffeeId: String) {
        viewModelScope.launch {
            repository.deleteCoffee(coffeeId)
            loadCoffees()
        }
    }
}
