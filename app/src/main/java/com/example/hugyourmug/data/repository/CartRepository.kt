package com.example.hugyourmug.data.repository

import com.example.hugyourmug.data.model.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CartRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun cartItemsRef() =
        firestore.collection("carts")
            .document(auth.currentUser!!.uid)
            .collection("items")

    suspend fun getCartItems(): List<CartItem> {
        val snapshot = cartItemsRef().get().await()
        return snapshot.documents.mapNotNull { it.toObject(CartItem::class.java) }
    }

    suspend fun addToCart(item: CartItem) {
        val query = cartItemsRef()
            .whereEqualTo("name", item.name)
            .get()
            .await()

        if (query.isEmpty) {
            cartItemsRef().add(item).await()
        } else {
            val doc = query.documents.first()
            val current = doc.toObject(CartItem::class.java)!!
            doc.reference.update("quantity", current.quantity + 1).await()
        }
    }

    suspend fun updateQuantity(itemId: String, quantity: Int) {
        cartItemsRef().document(itemId).update("quantity", quantity).await()
    }

    suspend fun removeItem(itemId: String) {
        cartItemsRef().document(itemId).delete().await()
    }

    suspend fun clearCart() {
        val snapshot = cartItemsRef().get().await()
        snapshot.documents.forEach { it.reference.delete().await() }
    }
}
