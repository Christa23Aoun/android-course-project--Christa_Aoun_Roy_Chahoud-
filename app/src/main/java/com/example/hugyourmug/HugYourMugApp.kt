package com.example.hugyourmug

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class HugYourMugApp : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099)

        val firestore = FirebaseFirestore.getInstance()
        firestore.useEmulator("10.0.2.2", 8080)
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
    }
}
