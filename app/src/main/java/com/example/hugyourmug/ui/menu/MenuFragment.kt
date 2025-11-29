package com.example.hugyourmug.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.R
import com.google.android.material.snackbar.Snackbar

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerMenu = view.findViewById<RecyclerView>(R.id.recyclerMenu)

        recyclerMenu.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CoffeeMenuAdapter(CoffeeMenuData.items) { item ->
            Snackbar.make(
                view,
                "${item.name} added to cart",
                Snackbar.LENGTH_SHORT
            ).show()
            // later: you can call your real Cart logic here
        }
        recyclerMenu.adapter = adapter
    }
}
