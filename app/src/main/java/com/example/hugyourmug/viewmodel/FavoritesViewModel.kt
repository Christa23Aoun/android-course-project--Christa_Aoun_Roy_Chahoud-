package com.example.hugyourmug.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hugyourmug.data.model.FavoriteItem
import com.example.hugyourmug.data.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val repository = FavoriteRepository()

    private val _favorites = MutableLiveData<List<FavoriteItem>>()
    val favorites: LiveData<List<FavoriteItem>> = _favorites

    fun loadFavorites(userId: String) {
        viewModelScope.launch {
            _favorites.value = repository.getFavorites(userId)
        }
    }

    fun addFavorite(item: FavoriteItem) {
        viewModelScope.launch {
            repository.addFavorite(item)
            loadFavorites(item.userId)
        }
    }

    fun removeFavorite(favoriteId: String, userId: String) {
        viewModelScope.launch {
            repository.removeFavorite(favoriteId)
            loadFavorites(userId)
        }
    }
}
