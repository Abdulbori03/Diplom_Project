package com.example.myproject.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.data.model.Product
import com.example.myproject.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _saveResult = MutableLiveData<Result<Unit>>()
    val saveResult: LiveData<Result<Unit>> = _saveResult

    fun saveProduct(name: String, barcode: String?, price: Double, category: String) {
        viewModelScope.launch {
            try {
                if (name.isBlank()) {
                    throw IllegalArgumentException("Название товара не может быть пустым")
                }

                if (price <= 0) {
                    throw IllegalArgumentException("Цена должна быть больше 0")
                }

                val product = Product(
                    id = 0, // Room will generate ID
                    name = name,
                    barcode = barcode?.takeIf { it.isNotBlank() },
                    price = price,
                    category = category,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                productRepository.insertProduct(product)
                _saveResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _saveResult.value = Result.failure(e)
            }
        }
    }

    fun updateProduct(id: Long, name: String, barcode: String?, price: Double, category: String) {
        viewModelScope.launch {
            try {
                if (name.isBlank()) {
                    throw IllegalArgumentException("Название товара не может быть пустым")
                }

                if (price <= 0) {
                    throw IllegalArgumentException("Цена должна быть больше 0")
                }

                val product = Product(
                    id = id,
                    name = name,
                    barcode = barcode?.takeIf { it.isNotBlank() },
                    price = price,
                    category = category,
                    updatedAt = System.currentTimeMillis()
                )
                productRepository.updateProduct(product)
                _saveResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _saveResult.value = Result.failure(e)
            }
        }
    }
} 