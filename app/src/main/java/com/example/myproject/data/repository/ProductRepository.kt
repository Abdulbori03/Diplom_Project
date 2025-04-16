package com.example.myproject.data.repository

import com.example.myproject.data.dao.ProductDao
import com.example.myproject.data.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {
    fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()
    
    fun getProductById(id: Long): Flow<Product> = productDao.getProductById(id)
    
    fun getProductsByGroup(groupId: Long): Flow<List<Product>> = productDao.getProductsByGroup(groupId)
    
    suspend fun insertProduct(product: Product) = productDao.insertProduct(product)
    
    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)
    
    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)

    suspend fun getProductByBarcode(barcode: String): Product? {
        return productDao.getProductByBarcode(barcode)
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts("%$query%")
    }
} 