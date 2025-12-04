package com.example.hugyourmug.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hugyourmug.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnMyProfile = view.findViewById<Button>(R.id.btnMyProfile)
        btnMyProfile.setOnClickListener {
            findNavController().navigate(R.id.navigation_profile)
        }

        return view
    }
}
