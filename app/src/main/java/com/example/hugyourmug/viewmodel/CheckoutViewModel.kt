package com.example.hugyourmug.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hugyourmug.data.model.CartItem
import com.example.hugyourmug.data.model.Order
import com.example.hugyourmug.data.model.OrderItem
import com.example.hugyourmug.data.repository.OrderRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class CheckoutViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val orderRepository = OrderRepository()

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    fun loadCart() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("carts")
            .document(uid)
            .collection("items")
            .get()
            .addOnSuccessListener { snapshot ->
                val items = snapshot.documents.mapNotNull {
                    it.toObject(CartItem::class.java)
                }
                _cartItems.value = items
            }
    }

    fun subtotal(): Double {
        return _cartItems.value?.sumOf { it.price * it.quantity } ?: 0.0
    }

    fun placeOrder(
        fullName: String,
        address: String,
        isDelivery: Boolean,
        bringChange: Boolean,
        onSuccess: () -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return
        val cartItems = _cartItems.value ?: return

        viewModelScope.launch {
            val subtotal = cartItems.sumOf { it.price * it.quantity }
            val tax = subtotal * 0.11
            val deliveryFee = if (isDelivery) 2.0 else 0.0
            val total = subtotal + tax + deliveryFee

            val order = Order(
                id = "",
                userId = uid,
                fullName = fullName,
                address = address,
                isDelivery = isDelivery,
                bringChange = bringChange,
                total = total,
                timestamp = System.currentTimeMillis()
            )

            val orderItems = cartItems.map {
                OrderItem(
                    id = "",
                    orderId = "",
                    name = it.name,
                    price = it.price,
                    quantity = it.quantity
                )
            }

            orderRepository.placeOrder(order, orderItems)

            firestore.collection("carts")
                .document(uid)
                .collection("items")
                .get()
                .addOnSuccessListener { snap ->
                    val clearBatch = firestore.batch()
                    snap.documents.forEach { clearBatch.delete(it.reference) }
                    clearBatch.commit()
                }

            _cartItems.value = emptyList()
            onSuccess()
        }
    }
}
