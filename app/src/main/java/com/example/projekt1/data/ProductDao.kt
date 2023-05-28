package com.example.projekt1.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.projekt1.data.model.ProductEntity


@Dao
interface ProductDao {
    @Query("SELECT * FROM product;")
    fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM product WHERE prodId = :id;")
    fun getMovie(id: Int): ProductEntity

    @Query("SELECT * FROM product ORDER BY price DESC;")
    fun getAllSortedByRating(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovie(newMovie: ProductEntity)

    @Update
    fun updateMovie(newMovie: ProductEntity)

    @Query("DELETE FROM product WHERE prodId = :id;")
    fun removeProduct(id: Int)

    @Query("SELECT COUNT(*) FROM product")
    fun count(): Int

    @Query("SELECT AVG(price) FROM product")
    fun averagePrice(): Double?

    @Query("SELECT SUM(price) FROM product")
    fun totalPrice(): Double?

}