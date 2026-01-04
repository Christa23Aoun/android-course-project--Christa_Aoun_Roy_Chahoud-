package com.example.hugyourmug.data.repository

import com.example.hugyourmug.data.model.Coffee
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CoffeeRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val collection = firestore.collection("coffees")

    suspend fun getAllCoffees(): List<Coffee> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { doc ->
            val coffee = doc.toObject(Coffee::class.java)
            coffee?.copy(id = doc.id)
        }
    }

    suspend fun addCoffee(coffee: Coffee) {
        collection.add(coffee.copy(id = "")).await()
    }

    suspend fun deleteCoffee(coffeeId: String) {
        collection.document(coffeeId).delete().await()
    }
}
