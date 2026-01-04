package com.example.hugyourmug.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hugyourmug.data.model.CartItem
import com.example.hugyourmug.data.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val repository = CartRepository()

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    fun loadCart() {
        viewModelScope.launch {
            _cartItems.value = repository.getCartItems()
        }
    }

    fun addToCart(item: CartItem) {
        viewModelScope.launch {
            repository.addToCart(item)
            loadCart()
        }
    }

    fun updateQuantity(itemId: String, quantity: Int) {
        viewModelScope.launch {
            repository.updateQuantity(itemId, quantity)
            loadCart()
        }
    }

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            repository.removeItem(itemId)
            loadCart()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
            loadCart()
        }
    }
}
