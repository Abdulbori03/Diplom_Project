package com.example.myproject.ui.reports

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.databinding.ActivityReportsBinding
import com.google.android.material.appbar.MaterialToolbar

class ReportsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupClickListeners()
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

            exportButton.setOnClickListener {
                // TODO: Export to Excel
            }

            moreButton.setOnClickListener {
                // TODO: Show more options
            }

            warehouseText.text = "Основной склад"
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            btnSalesReport.setOnClickListener {
                Toast.makeText(this@ReportsActivity, "Отчет по продажам", Toast.LENGTH_SHORT).show()
                // TODO: Show sales report
            }

            btnMoneyFlow.setOnClickListener {
                Toast.makeText(this@ReportsActivity, "Движение денег", Toast.LENGTH_SHORT).show()
                // TODO: Show money flow report
            }

            btnInventoryReport.setOnClickListener {
                Toast.makeText(this@ReportsActivity, "Инвентаризация", Toast.LENGTH_SHORT).show()
                // TODO: Show inventory report
            }
        }
    }
} 