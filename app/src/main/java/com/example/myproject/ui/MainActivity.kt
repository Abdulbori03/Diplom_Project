package com.example.myproject.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myproject.data.db.AppDatabase
import com.example.myproject.data.repository.ProductRepository
import com.example.myproject.databinding.ActivityMainBinding
import com.example.myproject.ui.documents.DocumentsActivity
import com.example.myproject.ui.expenses.ExpensesActivity
import com.example.myproject.ui.products.ProductsActivity
import com.example.myproject.ui.reports.ReportsActivity
import com.example.myproject.ui.scanner.BarcodeScannerActivity
import com.example.myproject.ui.viewmodel.ProductViewModel
import com.example.myproject.ui.viewmodel.ProductViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        val productDao = AppDatabase.getDatabase(this).productDao()
        val repository = ProductRepository(productDao)
        viewModel = ViewModelProvider(this, ProductViewModelFactory(repository))
            .get(ProductViewModel::class.java)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.apply {
            productsCard.setOnClickListener {
                startActivity(Intent(this@MainActivity, ProductsActivity::class.java))
            }

            documentsCard.setOnClickListener {
                startActivity(Intent(this@MainActivity, DocumentsActivity::class.java))
            }

            reportsCard.setOnClickListener {
                startActivity(Intent(this@MainActivity, ReportsActivity::class.java))
            }

            expensesCard.setOnClickListener {
                startActivity(Intent(this@MainActivity, ExpensesActivity::class.java))
            }

            fabScan.setOnClickListener {
                startActivity(Intent(this@MainActivity, BarcodeScannerActivity::class.java))
            }
        }

        binding.menuButton.setOnClickListener {
            // TODO: Open navigation drawer
        }

        binding.warehouseButton.setOnClickListener {
            // TODO: Open warehouse selection
        }
    }
} 