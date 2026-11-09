package com.example.dogbreedsgallery.navigation

sealed interface Route {
    data object Breeds : Route
    data object Favorites : Route
    data class BreedDetail(val breedId: Int) : Route
}