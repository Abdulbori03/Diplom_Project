package com.example.myproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.data.model.Product
import com.example.myproject.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()

    private val _saveResult = MutableStateFlow<Result<Unit>?>(null)
    val saveResult: StateFlow<Result<Unit>?> = _saveResult.asStateFlow()

    init {
        viewModelScope.launch {
            productRepository.getAllProducts().collect { products ->
                _products.value = products
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            try {
                productRepository.insertProduct(product)
                _saveResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _saveResult.value = Result.failure(e)
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            try {
                productRepository.updateProduct(product)
                _saveResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _saveResult.value = Result.failure(e)
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productRepository.deleteProduct(product)
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            productRepository.searchProducts(query).collect { results ->
                _searchResults.value = results
            }
        }
    }

    fun getProductById(id: Long) = productRepository.getProductById(id)
} 