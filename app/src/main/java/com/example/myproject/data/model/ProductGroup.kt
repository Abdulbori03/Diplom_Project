package com.example.myproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_groups")
data class ProductGroup(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val imageResId: Int,
    val description: String? = null
) 