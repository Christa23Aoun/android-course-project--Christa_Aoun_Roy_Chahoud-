package com.example.hugyourmug.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hugyourmug.R
import com.example.hugyourmug.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val txtUsername = view.findViewById<TextView>(R.id.txtUsername)
        val txtEmail = view.findViewById<TextView>(R.id.txtEmail)
        val btnMyOrders = view.findViewById<Button>(R.id.btnMyOrders)

        viewModel.user.observe(viewLifecycleOwner) { user ->
            txtUsername.text = user.username
            txtEmail.text = user.email
        }

        btnMyOrders.setOnClickListener {
            findNavController().navigate(R.id.orderHistoryFragment)
        }

        viewModel.loadUser()

        return view
    }
}
