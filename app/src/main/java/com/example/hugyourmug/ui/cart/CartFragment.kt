package com.example.hugyourmug.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.R
import com.example.hugyourmug.data.model.CartItem
import com.example.hugyourmug.viewmodel.CartViewModel

class CartFragment : Fragment() {

    private lateinit var recyclerCart: RecyclerView
    private lateinit var txtTotalPrice: TextView
    private lateinit var btnCheckout: Button

    private lateinit var adapter: CartAdapter
    private val viewModel: CartViewModel by viewModels()

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

        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.updateList(items)
            calculateTotal(items)
        }

        viewModel.loadCart()
    }

    private fun updateQuantity(item: CartItem, newQuantity: Int) {
        val itemId = item.id
        if (itemId.isBlank()) return
        viewModel.updateQuantity(itemId, newQuantity)
    }

    private fun deleteItem(item: CartItem) {
        val itemId = item.id
        if (itemId.isBlank()) return
        viewModel.removeItem(itemId)
    }

    private fun calculateTotal(items: List<CartItem>) {
        val total = items.sumOf { it.price * it.quantity }
        txtTotalPrice.text = "Total: $${String.format("%.2f", total)}"
    }
}
