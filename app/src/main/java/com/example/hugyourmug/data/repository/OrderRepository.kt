package com.example.hugyourmug.data.repository

import com.example.hugyourmug.data.model.Order
import com.example.hugyourmug.data.model.OrderItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val ordersCollection = firestore.collection("orders")

    suspend fun placeOrder(order: Order, items: List<OrderItem>) {
        val orderRef = ordersCollection.add(order).await()
        val itemsCollection = orderRef.collection("items")

        items.forEach { item ->
            itemsCollection.add(item.copy(orderId = orderRef.id)).await()
        }
    }

    suspend fun getOrdersForUser(userId: String): List<Order> {
        val snapshot = ordersCollection
            .whereEqualTo("userId", userId)
            .orderBy("timestamp")
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            val order = doc.toObject(Order::class.java)
            order?.copy(id = doc.id)
        }
    }

    suspend fun getOrderById(orderId: String): Order? {
        val doc = ordersCollection.document(orderId).get().await()
        val order = doc.toObject(Order::class.java)
        return order?.copy(id = doc.id)
    }

    suspend fun getOrderItems(orderId: String): List<OrderItem> {
        val snapshot = ordersCollection
            .document(orderId)
            .collection("items")
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            val item = doc.toObject(OrderItem::class.java)
            item?.copy(id = doc.id)
        }
    }
}
