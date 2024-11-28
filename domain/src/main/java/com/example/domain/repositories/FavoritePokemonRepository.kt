package com.example.domain.repositories

import kotlinx.coroutines.flow.Flow

interface FavoritePokemonRepository {
    val favorites: Flow<Set<String>>
    suspend fun addFavorite(name: String)
    suspend fun removeFavorite(name: String)
}