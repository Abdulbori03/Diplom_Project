package com.example.myproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val barcode: String?,
    val description: String?,
    val price: Double,
    val quantity: Int,
    val unit: String,
    val category: String?,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) 