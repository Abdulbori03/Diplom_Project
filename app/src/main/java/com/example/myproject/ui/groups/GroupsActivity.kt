package com.example.myproject.ui.groups

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.data.model.Group
import com.example.myproject.databinding.ActivityGroupsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupsBinding
    private val viewModel: GroupsViewModel by viewModels()
    private lateinit var adapter: GroupsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Группы"
    }

    private fun setupRecyclerView() {
        adapter = GroupsAdapter(
            onEditClick = { group ->
                // TODO: Открыть экран редактирования группы
            },
            onDeleteClick = { group ->
                // TODO: Показать диалог подтверждения удаления
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@GroupsActivity)
            adapter = this@GroupsActivity.adapter
        }
    }

    private fun setupClickListeners() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddGroupActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.groups.observe(this) { groups ->
            adapter.submitList(groups)
        }
    }

    private fun startEditGroupActivity(group: Group) {
        val intent = Intent(this, AddGroupActivity::class.java).apply {
            putExtra(AddGroupActivity.EXTRA_GROUP_ID, group.id)
            putExtra(AddGroupActivity.EXTRA_GROUP_NAME, group.name)
            putExtra(AddGroupActivity.EXTRA_GROUP_BARCODE, group.barcode)
        }
        startActivity(intent)
    }

    private fun showDeleteConfirmationDialog(group: Group) {
        AlertDialog.Builder(this)
            .setTitle("Удаление группы")
            .setMessage("Вы уверены, что хотите удалить группу \"${group.name}\"?")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.deleteGroup(group)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.example.myproject.R.menu.menu_groups, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
} 