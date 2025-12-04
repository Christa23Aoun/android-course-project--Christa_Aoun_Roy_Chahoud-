package com.example.hugyourmug.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hugyourmug.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val txtFullName = view.findViewById<TextView>(R.id.txtFullName)
        val txtEmail = view.findViewById<TextView>(R.id.txtEmail)
        val txtUsername = view.findViewById<TextView>(R.id.txtUsername)
        val btnMyOrders = view.findViewById<Button>(R.id.btnMyOrders)

        val prefs = requireContext().getSharedPreferences("userData", Context.MODE_PRIVATE)

        txtFullName.text = prefs.getString("fullName", "Full Name")
        txtEmail.text = prefs.getString("email", "Email")
        txtUsername.text = prefs.getString("username", "Username")

        btnMyOrders.setOnClickListener {
            findNavController().navigate(R.id.orderHistoryFragment)
        }

        return view
    }
}
