package com.example.projekt1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val prodId: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val image: String
)
