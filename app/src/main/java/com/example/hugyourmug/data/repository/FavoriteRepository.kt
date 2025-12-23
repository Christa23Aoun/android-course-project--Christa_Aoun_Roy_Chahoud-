package com.example.hugyourmug.data.repository

import com.example.hugyourmug.data.model.FavoriteItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FavoriteRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val collection = firestore.collection("favorites")

    suspend fun getFavorites(userId: String): List<FavoriteItem> {
        val snapshot = collection
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            val item = doc.toObject(FavoriteItem::class.java)
            item?.copy(id = doc.id)
        }
    }

    suspend fun addFavorite(item: FavoriteItem) {
        val existing = collection
            .whereEqualTo("userId", item.userId)
            .whereEqualTo("coffeeId", item.coffeeId)
            .get()
            .await()

        if (existing.documents.isEmpty()) {
            collection.add(item).await()
        }
    }

    suspend fun removeFavorite(favoriteId: String) {
        collection.document(favoriteId).delete().await()
    }
}
