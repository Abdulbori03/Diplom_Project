package com.example.myproject.ui.documents

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.databinding.ActivityDocumentsBinding
import com.google.android.material.tabs.TabLayout

class DocumentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDocumentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupTabLayout()
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
                // TODO: Show more options
            }
        }
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> loadAllDocuments()
                    1 -> loadIncomeDocuments()
                    2 -> loadOutcomeDocuments()
                    3 -> loadInventoryDocuments()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupFabButton() {
        binding.fabAdd.setOnClickListener {
            when (binding.tabLayout.selectedTabPosition) {
                1 -> openNewIncomeDocument()
                2 -> openNewOutcomeDocument()
                3 -> openNewInventoryDocument()
                else -> showDocumentTypeSelection()
            }
        }
    }

    private fun loadAllDocuments() {
        // TODO: Load all documents
    }

    private fun loadIncomeDocuments() {
        // TODO: Load income documents
    }

    private fun loadOutcomeDocuments() {
        // TODO: Load outcome documents
    }

    private fun loadInventoryDocuments() {
        // TODO: Load inventory documents
    }

    private fun openNewIncomeDocument() {
        // TODO: Open new income document
    }

    private fun openNewOutcomeDocument() {
        // TODO: Open new outcome document
    }

    private fun openNewInventoryDocument() {
        // TODO: Open new inventory document
    }

    private fun showDocumentTypeSelection() {
        // TODO: Show document type selection dialog
    }
} 