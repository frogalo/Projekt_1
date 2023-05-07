package com.example.projekt1.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.projekt1.data.model.MovieEntity


@Dao
interface MovieDao {
    @Query("SELECT * FROM movie;")
    fun getAll(): List<MovieEntity>

    @Query("SELECT * FROM movie ORDER BY name ASC;")
    fun getAllSortedByName(): List<MovieEntity>

    @Insert
    fun addDish(newDish: MovieEntity)

    @Update
    fun updateDish(newDish: MovieEntity)
}