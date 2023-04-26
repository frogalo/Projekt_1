package com.example.projekt1

interface Navigable {
    enum class Destination {
        List, Add
    }

    fun navigate(to: Destination)
}