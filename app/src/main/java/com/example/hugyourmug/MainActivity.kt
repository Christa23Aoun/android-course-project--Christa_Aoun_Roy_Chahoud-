package com.example.hugyourmug

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hugyourmug.ui.home.HomeFragment
import com.example.hugyourmug.ui.menu.MenuFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load Home screen by default
        loadFragment(HomeFragment())

        // Setup bottom navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.nav_menu -> {
                    loadFragment(MenuFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }
}
