package com.example.hugyourmug.data.repository

import com.example.hugyourmug.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    suspend fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null

        val snapshot = usersCollection
            .document(firebaseUser.uid)
            .get()
            .await()

        val user = snapshot.toObject(User::class.java)
        return user?.copy(id = firebaseUser.uid)
    }

    suspend fun createUser(
        firstName: String,
        lastName: String,
        username: String,
        email: String
    ) {
        val uid = auth.currentUser?.uid ?: return

        val user = User(
            id = uid,
            firstName = firstName,
            lastName = lastName,
            username = username,
            email = email
        )

        usersCollection.document(uid).set(user).await()
    }

    suspend fun updateUser(
        firstName: String,
        lastName: String,
        username: String
    ) {
        val uid = auth.currentUser?.uid ?: return

        val updates = mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "username" to username
        )

        usersCollection.document(uid).update(updates).await()
    }

    fun signOut() {
        auth.signOut()
    }
}
