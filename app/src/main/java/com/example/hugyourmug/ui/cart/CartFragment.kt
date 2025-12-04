package com.example.hugyourmug.ui.cart

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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import androidx.navigation.fragment.findNavController

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Button
import android.widget.TextView

class CartFragment : Fragment() {

    private lateinit var recyclerCart: RecyclerView
    private lateinit var txtTotalPrice: TextView
    private lateinit var btnCheckout: Button

    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerCart = view.findViewById(R.id.recyclerCart)
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice)
        btnCheckout = view.findViewById(R.id.btnCheckout)

        recyclerCart.layoutManager = LinearLayoutManager(requireContext())

        adapter = CartAdapter(
            onIncrease = { item -> updateQuantity(item, item.quantity + 1) },
            onDecrease = { item -> if (item.quantity > 1) updateQuantity(item, item.quantity - 1) },
            onDelete = { item -> deleteItem(item) }
        )

        recyclerCart.adapter = adapter

        btnCheckout.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
        }


        loadCart()
    }

    private fun getLoggedInUserId(): Int {
        val prefs = requireContext().getSharedPreferences("userData", Context.MODE_PRIVATE)
        return prefs.getInt("loggedInUserId", -1)
    }

    private fun loadCart() {
        val userId = getLoggedInUserId()
        if (userId == -1) return

        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val cartDao = db.cartDao()

            val items = withContext(Dispatchers.IO) {
                cartDao.getCartItemsForUser(userId)
            }

            adapter.updateList(items)
            calculateTotal(items)
        }
    }

    private fun updateQuantity(item: CartItem, newQuantity: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val cartDao = db.cartDao()

            withContext(Dispatchers.IO) {
                cartDao.updateQuantity(item.id, newQuantity)
            }

            loadCart()
        }
    }

    private fun deleteItem(item: CartItem) {
        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val cartDao = db.cartDao()

            withContext(Dispatchers.IO) {
                cartDao.deleteItemById(item.id)
            }

            loadCart()
        }
    }

    private fun calculateTotal(items: List<CartItem>) {
        val total = items.sumOf { it.price * it.quantity }
        txtTotalPrice.text = "Total: $${String.format("%.2f", total)}"
    }
}
