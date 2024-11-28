package com.example.data.repositories.favorites

import com.example.data.repositories.favorites.datastore.FavoritePokemonDataStore
import com.example.domain.repositories.FavoritePokemonRepository
import kotlinx.coroutines.flow.Flow

class FavoritePokemonRepositoryImpl(
    private val dataStore: FavoritePokemonDataStore
) : FavoritePokemonRepository {

    override val favorites: Flow<Set<String>> = dataStore.favorites

    override suspend fun addFavorite(name: String) {
        dataStore.addFavorite(name)
    }

    override suspend fun removeFavorite(name: String) {
        dataStore.removeFavorite(name)
    }
}