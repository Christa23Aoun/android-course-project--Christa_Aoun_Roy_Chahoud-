package com.example.hugyourmug.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hugyourmug.data.model.Order
import com.example.hugyourmug.data.model.OrderItem
import com.example.hugyourmug.data.repository.OrderRepository
import kotlinx.coroutines.launch

class OrdersViewModel : ViewModel() {

    private val repository = OrderRepository()

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    private val _orderItems = MutableLiveData<List<OrderItem>>()
    val orderItems: LiveData<List<OrderItem>> = _orderItems

    fun loadOrders(userId: String) {
        viewModelScope.launch {
            _orders.value = repository.getOrdersForUser(userId)
        }
    }

    fun placeOrder(order: Order, items: List<OrderItem>) {
        viewModelScope.launch {
            repository.placeOrder(order, items)
            loadOrders(order.userId)
        }
    }

    fun loadOrderById(orderId: String, onResult: (Order?) -> Unit) {
        viewModelScope.launch {
            onResult(repository.getOrderById(orderId))
        }
    }

    fun loadOrderItems(orderId: String) {
        viewModelScope.launch {
            _orderItems.value = repository.getOrderItems(orderId)
        }
    }
}
