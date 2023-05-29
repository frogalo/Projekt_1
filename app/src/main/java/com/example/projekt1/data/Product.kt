package com.example.projekt1.data

import android.net.Uri
import androidx.annotation.DrawableRes

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val image: Uri,
    val price: Double
)
