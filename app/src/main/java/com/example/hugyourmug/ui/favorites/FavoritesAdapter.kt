package com.example.hugyourmug.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.R
import com.example.hugyourmug.data.FavoriteItem

class FavoritesAdapter(
    private val items: MutableList<FavoriteItem>,
    private val onRemove: (FavoriteItem) -> Unit,
    private val onAddToCart: (FavoriteItem) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavViewHolder>() {

    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgFavCoffee)
        val txtName: TextView = itemView.findViewById(R.id.txtFavName)
        val txtPrice: TextView = itemView.findViewById(R.id.txtFavPrice)
        val btnRemove: ImageView = itemView.findViewById(R.id.btnRemoveFav)
        val btnAddToCart: Button = itemView.findViewById(R.id.btnFavAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val item = items[position]

        holder.img.setImageResource(item.imageResId)
        holder.txtName.text = item.name
        holder.txtPrice.text = "$${item.price}"

        holder.btnRemove.setOnClickListener { onRemove(item) }
        holder.btnAddToCart.setOnClickListener { onAddToCart(item) }
    }

    override fun getItemCount(): Int = items.size
}
