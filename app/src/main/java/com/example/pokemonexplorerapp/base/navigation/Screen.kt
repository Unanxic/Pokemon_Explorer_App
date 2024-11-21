package com.example.pokemonexplorerapp.base.navigation

enum class Screen(
    val route: String,
) {
    Splash("splash_screen"),
    HomeScreen("home_screen"),
    PokemonDetails("pokemon_details"),
    Favorites("favorites")
}