package com.example.myproject.ui.products

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myproject.R
import com.example.myproject.data.model.Group
import com.example.myproject.databinding.ActivityAddGroupBinding
import com.example.myproject.databinding.DialogColorPickerBinding
import com.example.myproject.ui.scanner.BarcodeScannerActivity
import com.example.myproject.ui.viewmodel.GroupViewModel
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class AddGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddGroupBinding
    private lateinit var dialogBinding: DialogColorPickerBinding
    private val viewModel: GroupViewModel by viewModels()
    private var selectedColor: Int = 0

    private val barcodeLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val barcode = result.data?.getStringExtra("barcode")
            barcode?.let {
                binding.barcodeInput.setText(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        dialogBinding = DialogColorPickerBinding.inflate(layoutInflater)
        
        // Setup color picker dialog
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Выберите цвет")
            .setView(dialogBinding.root)
            .create()

        // Setup color recycler view
        dialogBinding.colorRecyclerView.apply {
            layoutManager = GridLayoutManager(this@AddGroupActivity, 4)
            adapter = ColorAdapter(
                colors = resources.getIntArray(R.array.folder_colors).toList()
            ) { color ->
                selectedColor = color
                // Если colorPicker это MaterialCardView, используем setCardBackgroundColor, иначе setBackgroundColor
                try {
                    (binding.colorPicker as? com.google.android.material.card.MaterialCardView)?.setCardBackgroundColor(color)
                        ?: binding.colorPicker.setBackgroundColor(color)
                } catch (e: Exception) {
                    binding.colorPicker.setBackgroundColor(color)
                }
                dialog.dismiss()
            }
        }

        // Show color picker on click
        binding.colorPicker.setOnClickListener {
            dialog.show()
        }

        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Если штрих-код был передан при запуске
        intent.getStringExtra("barcode")?.let {
            binding.barcodeInput.setText(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_group, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_save -> {
                saveGroup()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openBarcodeScanner() {
        val intent = Intent(this, BarcodeScannerActivity::class.java)
        barcodeLauncher.launch(intent)
    }

    private fun saveGroup() {
        val name = binding.nameInput.text.toString().trim()
        if (name.isEmpty()) {
            binding.nameLayout.error = "Введите наименование"
            return
        }

        val group = Group(
            name = name,
            barcode = binding.barcodeInput.text?.toString(),
            color = selectedColor,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        viewModel.insertGroup(group)
        Toast.makeText(this, "Группа создана", Toast.LENGTH_SHORT).show()
        finish()
    }
} 