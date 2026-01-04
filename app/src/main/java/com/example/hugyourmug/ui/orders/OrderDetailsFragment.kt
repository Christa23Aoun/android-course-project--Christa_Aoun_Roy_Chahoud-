package com.example.hugyourmug.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hugyourmug.databinding.FragmentOrderDetailsBinding
import com.example.hugyourmug.viewmodel.OrdersViewModel
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailsFragment : Fragment() {

    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: OrderDetailsFragmentArgs by navArgs()
    private val viewModel: OrdersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerOrderItems.layoutManager =
            LinearLayoutManager(requireContext())

        val adapter = OrderDetailsItemsAdapter()
        binding.recyclerOrderItems.adapter = adapter

        viewModel.loadOrderById(args.orderId) { order ->
            if (order != null) {
                val sdf = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
                binding.txtOrderDate.text = sdf.format(Date(order.timestamp))
                binding.txtFullName.text = order.fullName
                binding.txtOrderType.text = if (order.isDelivery) "Delivery" else "Pickup"
                binding.txtAddress.text = if (order.isDelivery) order.address else "N/A"
                binding.txtBringChange.text =
                    if (order.isDelivery) {
                        if (order.bringChange) "Yes" else "No"
                    } else "N/A"
                binding.txtTotalPrice.text = "$%.2f".format(order.total)
            }
        }

        viewModel.orderItems.observe(viewLifecycleOwner) { items ->
            adapter.updateList(items)
        }

        viewModel.loadOrderItems(args.orderId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
