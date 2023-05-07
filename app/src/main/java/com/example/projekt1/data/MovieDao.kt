package com.example.projekt1.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.projekt1.data.model.MovieEntity


@Dao
interface MovieDao {
    @Query("SELECT * FROM movie;")
    fun getAll(): List<MovieEntity>

    @Query("SELECT * FROM movie WHERE movId = :id;")
    fun getMovie(id: Int): MovieEntity

    @Query("SELECT * FROM movie ORDER BY rating DESC;")
    fun getAllSortedByRating(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovie(newMovie: MovieEntity)

    @Update
    fun updateMovie(newMovie: MovieEntity)

    @Delete
    fun removeMovie(movieToDelete: MovieEntity)

    @Query("SELECT COUNT(*) FROM movie")
    fun count(): Int

    @Query("SELECT AVG(rating) FROM movie")
    fun averageRating(): Double?
}