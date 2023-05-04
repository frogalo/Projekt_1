package com.example.projekt1.model

import androidx.annotation.DrawableRes

data class Movie(
    val title: String,
    val description: String,
    @DrawableRes
    val movId: Int,
    val rating: Double
)
