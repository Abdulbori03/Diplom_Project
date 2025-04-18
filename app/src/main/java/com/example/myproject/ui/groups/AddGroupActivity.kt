package com.example.myproject.ui.groups

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myproject.data.db.AppDatabase
import com.example.myproject.data.model.Group
import com.example.myproject.data.repository.GroupRepository
import com.example.myproject.databinding.ActivityAddGroupBinding
import com.example.myproject.ui.adapter.ColorAdapter
import kotlinx.coroutines.launch

class AddGroupActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_GROUP_ID = "group_id"
        const val EXTRA_GROUP_NAME = "group_name"
        const val EXTRA_GROUP_BARCODE = "group_barcode"
    }

    private lateinit var binding: ActivityAddGroupBinding
    private lateinit var viewModel: GroupViewModel
    private lateinit var colorAdapter: ColorAdapter
    private var selectedColor = 0xFF000000.toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        setupToolbar()
        setupColorPicker()
        setupClickListeners()
        observeViewModel()
        loadGroupData()
    }

    private fun initViewModel() {
        val groupDao = AppDatabase.getDatabase(this).groupDao()
        val repository = GroupRepository(groupDao)
        viewModel = ViewModelProvider(this, GroupViewModel.Factory(repository))
            .get(GroupViewModel::class.java)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (intent.hasExtra(EXTRA_GROUP_ID)) "Редактирование группы" else "Новая группа"
    }

    private fun setupColorPicker() {
        val colors = listOf(
            0xFF000000.toInt(), // Черный
            0xFF2196F3.toInt(), // Синий
            0xFF4CAF50.toInt(), // Зеленый
            0xFFFFEB3B.toInt(), // Желтый
            0xFFF44336.toInt(), // Красный
            0xFF9C27B0.toInt(), // Фиолетовый
            0xFFFF9800.toInt(), // Оранжевый
            0xFF795548.toInt()  // Коричневый
        )

        colorAdapter = ColorAdapter(colors) { color ->
            selectedColor = color
            binding.colorPicker.setBackgroundColor(color)
        }

        binding.colorRecyclerView.apply {
            layoutManager = GridLayoutManager(this@AddGroupActivity, 4)
            adapter = colorAdapter
        }
    }

    private fun setupClickListeners() {
        binding.colorPicker.setOnClickListener {
            binding.colorRecyclerView.visibility = if (binding.colorRecyclerView.visibility == android.view.View.VISIBLE) {
                android.view.View.GONE
            } else {
                android.view.View.VISIBLE
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.insertResult.collect { result ->
                result?.let {
                    it.onSuccess {
                        Toast.makeText(this@AddGroupActivity, "Группа сохранена", Toast.LENGTH_SHORT).show()
                        finish()
                    }.onFailure { e ->
                        Toast.makeText(this@AddGroupActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun loadGroupData() {
        val groupId = intent.getLongExtra(EXTRA_GROUP_ID, -1)
        if (groupId != -1L) {
            lifecycleScope.launch {
                viewModel.getGroupById(groupId).collect { group ->
                    binding.nameInput.setText(group.name)
                    binding.barcodeInput.setText(group.barcode)
                    selectedColor = group.color
                    binding.colorPicker.setBackgroundColor(group.color)
                }
            }
        }
    }

    private fun saveGroup() {
        val name = binding.nameInput.text?.toString()?.trim()
        val barcode = binding.barcodeInput.text?.toString()?.trim()

        if (name.isNullOrEmpty()) {
            binding.nameInputLayout.error = "Введите название группы"
            return
        }

        val group = Group(
            id = intent.getLongExtra(EXTRA_GROUP_ID, 0),
            name = name,
            barcode = barcode ?: "",
            color = selectedColor
        )

        if (group.id == 0L) {
            viewModel.insertGroup(group)
        } else {
            viewModel.updateGroup(group)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.example.myproject.R.menu.menu_add_group, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            com.example.myproject.R.id.action_save -> {
                saveGroup()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
} 