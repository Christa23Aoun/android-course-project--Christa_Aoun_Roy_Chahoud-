package com.example.hugyourmug.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hugyourmug.R
import com.example.hugyourmug.data.AppDatabase
import com.example.hugyourmug.data.CartItem
import com.example.hugyourmug.data.FavoriteItem
import com.example.hugyourmug.databinding.FragmentFavoritesBinding
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = AppDatabase.getDatabase(requireContext())

        binding.recyclerFavorites.layoutManager = LinearLayoutManager(requireContext())

        loadFavorites()
    }

    private fun loadFavorites() {
        lifecycleScope.launch {
            val userId = 1
            val favorites = db.favoriteDao().getFavoritesForUser(userId).toMutableList()

            val adapter = FavoritesAdapter(
                items = favorites,
                onRemove = { item ->
                    lifecycleScope.launch {
                        db.favoriteDao().delete(item)
                        loadFavorites()
                    }
                },
                onAddToCart = { item ->
                    lifecycleScope.launch {
                        db.cartDao().insert(
                            CartItem(
                                userId = item.userId,
                                name = item.name,
                                price = item.price,
                                imageResId = item.imageResId,
                                quantity = 1
                            )
                        )
                    }
                }
            )

            binding.recyclerFavorites.adapter = adapter
        }
    }
}
