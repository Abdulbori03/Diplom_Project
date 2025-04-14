package com.example.myproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myproject.data.model.Product
import com.example.myproject.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allProducts.collect { products ->
                _products.value = products
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            repository.searchProducts(query).collect { results ->
                _searchResults.value = results
            }
        }
    }
}

class ProductViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 