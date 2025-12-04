package com.example.hugyourmug.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hugyourmug.data.AppDatabase
import com.example.hugyourmug.databinding.FragmentOrderHistoryBinding
import kotlinx.coroutines.launch

class OrderHistoryFragment : Fragment() {

    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerOrderHistory.layoutManager =
            LinearLayoutManager(requireContext())

        loadOrders()
    }

    private fun loadOrders() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val orders = db.orderDao().getOrdersForUser(1)

            binding.recyclerOrderHistory.adapter =
                OrderHistoryAdapter(orders) { selectedOrder ->
                    // Will open order details screen
                }
        }
    }
}
