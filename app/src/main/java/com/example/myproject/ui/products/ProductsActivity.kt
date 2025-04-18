package com.example.myproject.ui.products

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.data.db.AppDatabase
import com.example.myproject.data.model.Product
import com.example.myproject.data.repository.ProductRepository
import com.example.myproject.databinding.ActivityProductsBinding
import com.example.myproject.ui.groups.GroupsActivity
import com.example.myproject.ui.viewmodel.ProductViewModel
import com.example.myproject.ui.viewmodel.ProductViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class ProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductsBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        initViewModel()
        setupRecyclerView()
        setupClickListeners()
        observeProducts()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.apply {
            menuButton.setOnClickListener {
                // TODO: Open navigation drawer
            }

            searchButton.setOnClickListener {
                // TODO: Implement search
            }

            sortButton.setOnClickListener {
                // TODO: Show sort options
            }

            moreButton.setOnClickListener {
                // TODO: Show more options
            }
        }
    }

    private fun initViewModel() {
        val productDao = AppDatabase.getDatabase(this).productDao()
        val repository = ProductRepository(productDao)
        viewModel = ViewModelProvider(this, ProductViewModelFactory(repository))
            .get(ProductViewModel::class.java)
    }

    private fun setupRecyclerView() {
        adapter = ProductsAdapter { product ->
            Toast.makeText(this, "Выбран товар: ${product.name}", Toast.LENGTH_SHORT).show()
            // TODO: Open product details
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProductsActivity)
            adapter = this@ProductsActivity.adapter
        }
    }

    private fun setupClickListeners() {
        binding.addProductButton.setOnClickListener {
            Toast.makeText(this, "Добавление товара", Toast.LENGTH_SHORT).show()
            // TODO: Open add product dialog
        }

        binding.addGroupButton.setOnClickListener {
            val intent = Intent(this, GroupsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeProducts() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.products.collect { products ->
                    adapter.submitList(products)
                    updateProductCount(products)
                }
            }
        }
    }

    private fun updateProductCount(products: List<Product>) {
        binding.totalCountText.text = "${products.size}"
    }
} 