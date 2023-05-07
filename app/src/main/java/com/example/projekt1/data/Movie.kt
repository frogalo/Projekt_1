package com.example.projekt1.data

import androidx.annotation.DrawableRes

data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    @DrawableRes
    val movId: Int,
    val rating: Double
)
