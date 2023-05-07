package com.example.projekt1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val movId: Int = 0,
    val title: String,
    val description: String,
    val rating: Double,
    val cover: String
)
