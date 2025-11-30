package com.example.hugyourmug.ui.menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.R
import com.example.hugyourmug.data.AppDatabase
import com.example.hugyourmug.data.CartItem
import com.example.hugyourmug.data.FavoriteItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuFragment : Fragment() {

    private lateinit var recyclerMenu: RecyclerView
    private lateinit var adapter: CoffeeMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerMenu = view.findViewById(R.id.recyclerMenu)
        recyclerMenu.layoutManager = LinearLayoutManager(requireContext())

        adapter = CoffeeMenuAdapter(
            items = CoffeeMenuData.items,
            onAddToCartClick = { item ->
                addToCart(item)
            },
            onAddToFavoriteClick = { item ->
                toggleFavorite(item)
            }
        )


        recyclerMenu.adapter = adapter
    }

    private fun getLoggedInUserId(): Int {
        val prefs = requireContext().getSharedPreferences("userData", Context.MODE_PRIVATE)
        return prefs.getInt("loggedInUserId", -1)
    }

    private fun addToCart(item: CoffeeMenuItem) {
        val userId = getLoggedInUserId()
        if (userId == -1) {
            Snackbar.make(requireView(), "Please log in again", Snackbar.LENGTH_SHORT).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val cartDao = db.cartDao()

            withContext(Dispatchers.IO) {
                val existing = cartDao.getCartItemForUserAndName(userId, item.name)
                if (existing == null) {
                    cartDao.insert(
                        CartItem(
                            userId = userId,
                            name = item.name,
                            price = item.price,
                            imageResId = item.imageResId,
                            quantity = 1
                        )
                    )
                } else {
                    cartDao.updateQuantity(existing.id, existing.quantity + 1)
                }
            }

            Snackbar.make(
                requireView(),
                "${item.name} added to your cart",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun toggleFavorite(item: CoffeeMenuItem) {
        val userId = getLoggedInUserId()
        if (userId == -1) {
            Snackbar.make(requireView(), "Please log in again", Snackbar.LENGTH_SHORT).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val favoriteDao = db.favoriteDao()

            val wasAdded = withContext(Dispatchers.IO) {
                val existing = favoriteDao.getFavoriteForUserAndName(userId, item.name)
                return@withContext if (existing == null) {
                    favoriteDao.insert(
                        FavoriteItem(
                            userId = userId,
                            name = item.name,
                            price = item.price,
                            imageResId = item.imageResId
                        )
                    )
                    true
                } else {
                    favoriteDao.delete(existing)
                    false
                }
            }

            val message = if (wasAdded) {
                "${item.name} added to favorites ♥"
            } else {
                "${item.name} removed from favorites"
            }

            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        }
    }
}
