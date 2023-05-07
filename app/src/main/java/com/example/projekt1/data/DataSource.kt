package com.example.projekt1.data

import com.example.projekt1.R

object DataSource {
    val movies = mutableListOf<Movie>(
        Movie(
            1, "Flash", "Speed description", R.drawable.flash_ver6, 7.8,
        ),
        Movie(
            2, "Jhon Wick 4",
            "John Wick odkrywa sposób na pokonanie Gildii Zabójców. Zanim jednak odzyska wolność, będzie musiał stawić czoła nowemu wrogowi i jego sojusznikom, z którymi stoczy walki na kilku kontynentach.",
            R.drawable.john_wick_chapter_four_ver2, 8.5,
        ),
        Movie(
            3, "Renfield", "He's Dracula", R.drawable.renfield, 4.9,
        ),
        Movie(
            4, "Super Mario Bros The Movie",
            "Like a game",
            R.drawable.super_mario_bros_the_movie_ver10, 6.6,
        ),
        Movie(
            5, "Turning Red", "Kids movie", R.drawable.turning_red, 7.1,
        ),
    )
}