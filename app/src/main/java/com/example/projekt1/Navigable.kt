package com.example.projekt1

interface Navigable {
    enum class Destination {
        List, Add, Edit
    }

    fun navigate(to: Destination, id: Int? = null)
}