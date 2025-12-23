package com.example.hugyourmug.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hugyourmug.databinding.FragmentCheckoutBinding
import com.example.hugyourmug.viewmodel.CheckoutViewModel

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerCheckoutItems.layoutManager =
            LinearLayoutManager(requireContext())

        val adapter = CheckoutItemsAdapter()
        binding.recyclerCheckoutItems.adapter = adapter

        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.updateList(items)
            updateTotals()
        }

        setupListeners()
        viewModel.loadCart()
    }

    private fun setupListeners() {
        binding.rbPickup.setOnClickListener { updateTotals() }
        binding.rbDelivery.setOnClickListener { updateTotals() }

        binding.btnPlaceOrder.setOnClickListener {
            placeOrder()
        }
    }

    private fun updateTotals() {
        val subtotal = viewModel.subtotal()
        val tax = subtotal * 0.11
        val deliveryFee = if (binding.rbDelivery.isChecked) 2.0 else 0.0
        val total = subtotal + tax + deliveryFee

        binding.txtSubtotalValue.text = "$%.2f".format(subtotal)
        binding.txtTaxValue.text = "$%.2f".format(tax)
        binding.txtDeliveryFeeValue.text = "$%.2f".format(deliveryFee)
        binding.txtTotalValue.text = "$%.2f".format(total)
    }

    private fun placeOrder() {
        val fullName = binding.edtFullName.text.toString().trim()
        val address = binding.edtAddress.text.toString().trim()
        val isDelivery = binding.rbDelivery.isChecked
        val bringChange = binding.chkBringChange.isChecked

        if (fullName.isEmpty()) {
            Toast.makeText(requireContext(), "Enter your full name", Toast.LENGTH_SHORT).show()
            return
        }

        if (isDelivery && address.isEmpty()) {
            Toast.makeText(requireContext(), "Enter your address", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.placeOrder(
            fullName = fullName,
            address = address,
            isDelivery = isDelivery,
            bringChange = bringChange,
            onSuccess = {
                Toast.makeText(requireContext(), "Order placed successfully ☕", Toast.LENGTH_LONG).show()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
