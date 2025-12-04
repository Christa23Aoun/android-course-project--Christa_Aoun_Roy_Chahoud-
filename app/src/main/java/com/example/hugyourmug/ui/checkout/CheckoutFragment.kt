package com.example.hugyourmug.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hugyourmug.data.AppDatabase
import com.example.hugyourmug.data.CartItem
import com.example.hugyourmug.data.Order
import com.example.hugyourmug.data.OrderItem

import com.example.hugyourmug.databinding.FragmentCheckoutBinding
import kotlinx.coroutines.launch

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase
    private var cartItems: List<CartItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = AppDatabase.getDatabase(requireContext())

        binding.recyclerCheckoutItems.layoutManager =
            LinearLayoutManager(requireContext())

        loadCartItems()
        setupListeners()
    }

    private fun loadCartItems() {
        lifecycleScope.launch {
            val userId = 1
            cartItems = db.cartDao().getCartItemsForUser(userId)

            binding.recyclerCheckoutItems.adapter =
                CheckoutItemsAdapter(cartItems)

            calculateTotals()
        }
    }

    private fun calculateTotals() {
        val subtotal = cartItems.sumOf { it.price * it.quantity }
        val tax = subtotal * 0.11
        val deliveryFee = if (binding.rbDelivery.isChecked) 2.0 else 0.0
        val total = subtotal + tax + deliveryFee

        binding.txtSubtotalValue.text = "$%.2f".format(subtotal)
        binding.txtTaxValue.text = "$%.2f".format(tax)
        binding.txtDeliveryFeeValue.text = "$%.2f".format(deliveryFee)
        binding.txtTotalValue.text = "$%.2f".format(total)
    }

    private fun setupListeners() {
        binding.rbPickup.setOnClickListener { calculateTotals() }
        binding.rbDelivery.setOnClickListener { calculateTotals() }

        binding.btnPlaceOrder.setOnClickListener {
            placeOrder()
        }
    }

    private fun placeOrder() {
        val fullName = binding.edtFullName.text.toString().trim()
        val address = binding.edtAddress.text.toString().trim()
        val isDelivery = binding.rbDelivery.isChecked
        val bringChange = binding.chkBringChange.isChecked
        val userId = 1  // replace later with real logged user ID

        if (fullName.isEmpty()) {
            Toast.makeText(requireContext(), "Enter your full name", Toast.LENGTH_SHORT).show()
            return
        }

        if (isDelivery && address.isEmpty()) {
            Toast.makeText(requireContext(), "Enter your address", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val subtotal = cartItems.sumOf { it.price * it.quantity }
            val tax = subtotal * 0.11
            val deliveryFee = if (isDelivery) 2.0 else 0.0
            val total = subtotal + tax + deliveryFee

            // 1️⃣ CREATE ORDER OBJECT
            val order = com.example.hugyourmug.data.Order(
                userId = userId,
                fullName = fullName,
                address = address,
                isDelivery = isDelivery,
                bringChange = bringChange,
                total = total,
                timestamp = System.currentTimeMillis()
            )

            // 2️⃣ INSERT ORDER - GET ORDER ID
            val orderId = db.orderDao().insert(order).toInt()

            // 3️⃣ CREATE ORDER ITEMS
            val orderItems = cartItems.map { cartItem ->
                com.example.hugyourmug.data.OrderItem(
                    orderId = orderId,
                    name = cartItem.name,
                    price = cartItem.price,
                    quantity = cartItem.quantity,
                    imageResId = cartItem.imageResId
                )
            }

            // 4️⃣ INSERT ORDER ITEMS
            db.orderItemDao().insertAll(orderItems)

            // 5️⃣ CLEAR CART
            db.cartDao().clearCartForUser(userId)

            // 6️⃣ SHOW SUCCESS MESSAGE
            Toast.makeText(requireContext(), "Order placed successfully ☕", Toast.LENGTH_LONG).show()

            // 7️⃣ OPTIONAL: NAVIGATE BACK TO HOME
            // findNavController().navigate(R.id.navigation_home)
        }
    }

}
