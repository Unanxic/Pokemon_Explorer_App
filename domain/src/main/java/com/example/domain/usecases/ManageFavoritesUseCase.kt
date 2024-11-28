package com.example.domain.usecases

import com.example.domain.repositories.FavoritePokemonRepository
import kotlinx.coroutines.flow.Flow

class ManageFavoritesUseCase(
    private val favoritePokemonRepository: FavoritePokemonRepository
) {

    val favorites: Flow<Set<String>> = favoritePokemonRepository.favorites

    suspend fun addFavorite(name: String) {
        favoritePokemonRepository.addFavorite(name)
    }

    suspend fun removeFavorite(name: String) {
        favoritePokemonRepository.removeFavorite(name)
    }

    suspend fun toggleFavorite(name: String, isFavorite: Boolean) {
        if (isFavorite) {
            favoritePokemonRepository.removeFavorite(name)
        } else {
            favoritePokemonRepository.addFavorite(name)
        }
    }
}