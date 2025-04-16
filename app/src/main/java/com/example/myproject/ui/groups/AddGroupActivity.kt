package com.example.myproject.ui.groups

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myproject.R
import com.example.myproject.data.model.Group
import com.example.myproject.databinding.ActivityAddGroupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddGroupBinding
    private val viewModel: AddGroupViewModel by viewModels()
    private var selectedColor: Int = 0

    companion object {
        const val EXTRA_GROUP_ID = "extra_group_id"
        const val EXTRA_GROUP_NAME = "extra_group_name"
        const val EXTRA_GROUP_BARCODE = "extra_group_barcode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Новая группа"
    }

    private fun setupClickListeners() {
        binding.apply {
            barcodeInput.setOnClickListener {
                // TODO: Открыть сканер штрих-кода
            }

            colorPicker.setOnClickListener {
                // TODO: Открыть диалог выбора цвета
            }

            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    android.R.id.home -> {
                        finish()
                        true
                    }
                    R.id.action_save -> {
                        saveGroup()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun saveGroup() {
        val name = binding.nameInput.text.toString().trim()
        if (name.isEmpty()) {
            binding.nameLayout.error = "Введите название группы"
            return
        }

        val group = Group(
            name = name,
            barcode = binding.barcodeInput.text?.toString(),
            color = selectedColor,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        lifecycleScope.launch {
            viewModel.insertGroup(group)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.insertResult.collect { result ->
                result?.onSuccess {
                    Toast.makeText(this@AddGroupActivity, "Группа добавлена", Toast.LENGTH_SHORT).show()
                    finish()
                }?.onFailure { error ->
                    Toast.makeText(this@AddGroupActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
} 