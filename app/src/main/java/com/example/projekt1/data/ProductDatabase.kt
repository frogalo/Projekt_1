package com.example.projekt1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projekt1.data.model.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ProductDatabase : RoomDatabase() {
    abstract val products: ProductDao

    companion object {
        fun open(context: Context): ProductDatabase = Room.databaseBuilder(
            context, ProductDatabase::class.java, "products.db"
        ).build()
    }
}