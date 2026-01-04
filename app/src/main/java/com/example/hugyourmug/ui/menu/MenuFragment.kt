package com.example.hugyourmug.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.R
import com.example.hugyourmug.data.model.Coffee
import com.example.hugyourmug.viewmodel.CoffeeViewModel
import com.google.android.material.snackbar.Snackbar

class MenuFragment : Fragment() {

    private lateinit var recyclerMenu: RecyclerView
    private lateinit var adapter: CoffeeMenuAdapter
    private val viewModel: CoffeeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerMenu = view.findViewById(R.id.recyclerMenu)
        recyclerMenu.layoutManager = LinearLayoutManager(requireContext())

        adapter = CoffeeMenuAdapter(
            items = emptyList(),
            onAddClick = { coffee ->
                onAddCoffee(coffee)
            },
            onDeleteClick = { coffee ->
                onDeleteCoffee(coffee)
            }
        )

        recyclerMenu.adapter = adapter

        viewModel.allCoffees.observe(viewLifecycleOwner) { coffees ->
            adapter.updateData(coffees)
        }
    }

    private fun onAddCoffee(coffee: Coffee) {
        viewModel.addCoffee(coffee)
        Snackbar.make(requireView(), "${coffee.name} added", Snackbar.LENGTH_SHORT).show()
    }

    private fun onDeleteCoffee(coffee: Coffee) {
        viewModel.deleteCoffee(coffee.id)
        Snackbar.make(requireView(), "${coffee.name} deleted", Snackbar.LENGTH_SHORT).show()
    }
}
