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
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                productRepository.getAllProducts().collect { products ->
                    _products.value = products
                }
            } catch (e: Exception) {
                // TODO: Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshProducts() {
        loadProducts()
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            try {
                productRepository.deleteProduct(product)
                loadProducts() // Refresh the list after deletion
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
} 