package com.example.hugyourmug

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.example.hugyourmug.ui.home.HomeFragment
import com.example.hugyourmug.ui.menu.MenuFragment
import com.example.hugyourmug.ui.cart.CartFragment
import com.example.hugyourmug.ui.favorites.FavoritesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        loadFragment(HomeFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.navigation_menu -> {
                    loadFragment(MenuFragment())
                    true
                }

                R.id.navigation_cart -> {
                    loadFragment(CartFragment())
                    true
                }

                R.id.navigation_favorites -> {
                    loadFragment(FavoritesFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}
