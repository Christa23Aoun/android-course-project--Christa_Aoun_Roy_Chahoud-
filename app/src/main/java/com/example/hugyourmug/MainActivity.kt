package com.example.hugyourmug
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hugyourmug.data.Coffee
import com.example.hugyourmug.ui.CoffeeAdapter
import com.example.hugyourmug.viewmodel.CoffeeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CoffeeAdapter
    private lateinit var fabAdd: FloatingActionButton


    private val coffeeViewModel: CoffeeViewModel by viewModels {
        object : ViewModelProvider.AndroidViewModelFactory(application) {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CoffeeViewModel(application) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rvCoffees)
        fabAdd = findViewById(R.id.fabAddCoffee)

        adapter = CoffeeAdapter(emptyList()) { coffee ->
            Toast.makeText(this, "Selected: ${coffee.name}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val swipeToDeleteCallback = object : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val coffee = adapter.getItemAt(position)
                coffeeViewModel.deleteCoffee(coffee)
                Toast.makeText(this@MainActivity, "${coffee.name} deleted", Toast.LENGTH_SHORT).show()
            }
        }
        androidx.recyclerview.widget.ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(recyclerView)

        // Observe LiveData
        coffeeViewModel.allCoffees.observe(this) { list ->
            adapter.updateData(list)
        }

        fabAdd.setOnClickListener { showAddCoffeeDialog() }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_samples -> {
                val samples = listOf(
                    Coffee(name = "Latte", price = 4.0, description = "Smooth espresso with milk"),
                    Coffee(name = "Cappuccino", price = 4.5, description = "Foamy milk and espresso"),
                    Coffee(name = "Espresso", price = 3.0, description = "Strong and rich shot")
                )
                samples.forEach { coffeeViewModel.addCoffee(it) }
                Toast.makeText(this, "Sample coffees added!", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.action_clear_all -> {
                coffeeViewModel.deleteAll()
                Toast.makeText(this, "All coffees cleared!", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAddCoffeeDialog() {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_add_coffee, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.edtCoffeeName)
        val priceInput = dialogView.findViewById<EditText>(R.id.edtCoffeePrice)
        val descInput = dialogView.findViewById<EditText>(R.id.edtCoffeeDesc)

        AlertDialog.Builder(this)
            .setTitle("Add New Coffee")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString().trim()
                val price = priceInput.text.toString().toDoubleOrNull() ?: 0.0
                val desc = descInput.text.toString().trim()

                if (name.isNotEmpty()) {
                    val coffee = Coffee(name = name, price = price, description = desc)
                    coffeeViewModel.addCoffee(coffee)
                    Toast.makeText(this, "Coffee added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
