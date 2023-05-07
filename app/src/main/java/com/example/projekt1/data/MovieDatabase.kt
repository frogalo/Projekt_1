package com.example.projekt1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projekt1.data.model.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movies: MovieDao

    companion object {
        fun open(context: Context): MovieDatabase = Room.databaseBuilder(
            context, MovieDatabase::class.java, "movies.db"
        ).build()
    }
}