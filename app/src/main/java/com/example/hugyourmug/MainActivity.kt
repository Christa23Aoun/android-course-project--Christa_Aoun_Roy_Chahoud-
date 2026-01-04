package com.example.hugyourmug

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hugyourmug.ui.maps.MapsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val navController = findNavController(R.id.nav_host_fragment)

        bottomNav.setupWithNavController(navController)

        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nav_maps) {
                startActivity(Intent(this, MapsActivity::class.java))
                false
            } else {
                true
            }
        }
    }
}
