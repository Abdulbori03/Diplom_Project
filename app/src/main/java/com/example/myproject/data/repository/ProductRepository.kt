package com.example.myproject.data.repository

import com.example.myproject.data.dao.ProductDao
import com.example.myproject.data.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    suspend fun insertProduct(product: Product): Long {
        return productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun getProductById(id: Long): Product? {
        return productDao.getProductById(id)
    }

    suspend fun getProductByBarcode(barcode: String): Product? {
        return productDao.getProductByBarcode(barcode)
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts("%$query%")
    }
} 