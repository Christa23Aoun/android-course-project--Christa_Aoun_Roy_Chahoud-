package com.example.hugyourmug.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hugyourmug.databinding.FragmentOrderHistoryBinding
import com.example.hugyourmug.viewmodel.OrdersViewModel
import com.google.firebase.auth.FirebaseAuth

class OrderHistoryFragment : Fragment() {

    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrdersViewModel by viewModels()

    private val userId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

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

        val adapter = OrderHistoryAdapter { order ->
            val action =
                OrderHistoryFragmentDirections
                    .actionOrderHistoryFragmentToOrderDetailsFragment(order.id)
            findNavController().navigate(action)
        }

        binding.recyclerOrderHistory.adapter = adapter

        viewModel.orders.observe(viewLifecycleOwner) { orders ->
            adapter.updateList(orders)
        }

        if (userId.isNotEmpty()) {
            viewModel.loadOrders(userId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
