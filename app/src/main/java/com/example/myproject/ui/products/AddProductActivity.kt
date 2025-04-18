package com.example.myproject.ui.products

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.lifecycle.lifecycleScope
import com.example.myproject.R
import com.example.myproject.data.model.Product
import com.example.myproject.databinding.ActivityAddProductBinding
import com.example.myproject.ui.scanner.BarcodeScannerActivity
import com.example.myproject.ui.viewmodel.GroupViewModel
import com.example.myproject.ui.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddProductActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PRODUCT_ID = "product_id"
    }

    private lateinit var binding: ActivityAddProductBinding
    private val productViewModel: ProductViewModel by viewModels()
    private val groupViewModel: GroupViewModel by viewModels()
    private var selectedGroupId: Long? = null

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
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViews()
        setupDropdowns()
        observeViewModel()
        loadProductData()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (intent.hasExtra(EXTRA_PRODUCT_ID)) {
            "Редактировать товар"
        } else {
            "Новый товар"
        }
    }

    private fun setupViews() {
        binding.apply {
            barcodeInput.setOnClickListener {
                openBarcodeScanner()
            }

            backButton.setOnClickListener {
                finish()
            }

            saveButton.setOnClickListener {
                saveProduct()
            }

            decreaseQuantityButton.setOnClickListener {
                val currentQuantity = quantityInput.text.toString().toDoubleOrNull() ?: 0.0
                if (currentQuantity > 0) {
                    quantityInput.setText((currentQuantity - 1).toString())
                }
            }

            increaseQuantityButton.setOnClickListener {
                val currentQuantity = quantityInput.text.toString().toDoubleOrNull() ?: 0.0
                quantityInput.setText((currentQuantity + 1).toString())
            }
        }
    }

    private fun setupDropdowns() {
        lifecycleScope.launch {
            val groups = groupViewModel.allGroups.first()
            val groupNames = groups.map { it.name }
            val groupsAdapter = ArrayAdapter<String>(
                this@AddProductActivity,
                android.R.layout.simple_dropdown_item_1line,
                groupNames
            )
            
            (binding.groupInput as AppCompatAutoCompleteTextView).apply {
                setAdapter(groupsAdapter)
                setOnItemClickListener { _, _, position, _ ->
                    selectedGroupId = groups[position].id
                }
            }
        }
    }

    private fun openBarcodeScanner() {
        val intent = Intent(this, BarcodeScannerActivity::class.java)
        barcodeLauncher.launch(intent)
    }

    private fun saveProduct() {
        val name = binding.nameInput.text.toString().trim()
        if (name.isEmpty()) {
            binding.nameInputLayout.error = "Введите наименование"
            return
        }

        val quantity = binding.quantityInput.text.toString().toDoubleOrNull() ?: 0.0

        val product = Product(
            name = name,
            barcode = binding.barcodeInput.text?.toString(),
            price = quantity,
            category = "",
            groupId = selectedGroupId,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        lifecycleScope.launch {
            if (intent.hasExtra(EXTRA_PRODUCT_ID)) {
                productViewModel.updateProduct(product)
            } else {
                productViewModel.addProduct(product)
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            productViewModel.saveResult.collect { result ->
                result?.onSuccess {
                    Toast.makeText(this@AddProductActivity, "Товар сохранен", Toast.LENGTH_SHORT).show()
                    finish()
                }?.onFailure { error ->
                    Toast.makeText(this@AddProductActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadProductData() {
        val productId = intent.getLongExtra(EXTRA_PRODUCT_ID, 0)
        if (productId > 0) {
            lifecycleScope.launch {
                productViewModel.getProductById(productId).collect { product ->
                    product?.let {
                        binding.nameInput.setText(it.name)
                        binding.barcodeInput.setText(it.barcode)
                        binding.quantityInput.setText(it.price.toString())
                        selectedGroupId = it.groupId
                        
                        // Установка выбранной группы
                        if (it.groupId != null) {
                            val groups = groupViewModel.allGroups.first()
                            val group = groups.find { g -> g.id == it.groupId }
                            group?.let { g ->
                                (binding.groupInput as AppCompatAutoCompleteTextView).setText(g.name)
                            }
                        }
                    }
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