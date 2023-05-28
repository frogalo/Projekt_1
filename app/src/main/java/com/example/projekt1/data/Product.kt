package com.example.projekt1.data

import androidx.annotation.DrawableRes

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    @DrawableRes
    val prodId: Int,
    val price: Double
)
