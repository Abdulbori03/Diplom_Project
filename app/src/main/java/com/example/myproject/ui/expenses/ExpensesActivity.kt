package com.example.myproject.ui.expenses

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.databinding.ActivityExpensesBinding

class ExpensesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpensesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpensesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupFabButton()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            binding.menuButton.setOnClickListener {
                // TODO: Open navigation drawer
            }

            binding.searchButton.setOnClickListener {
                // TODO: Open search
            }

            binding.exportButton.setOnClickListener {
                // TODO: Export to Excel
            }

            binding.moreButton.setOnClickListener {
                // TODO: Show more options menu with categories
            }
        }
    }

    private fun setupFabButton() {
        binding.fabAdd.setOnClickListener {
            // TODO: Open add expense dialog
        }
    }
} 