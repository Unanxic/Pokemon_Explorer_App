package com.example.data.repositories.favorites.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val DATASTORE_NAME = "favorite_pokemon"

val Context.favoritePokemonDataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class FavoritePokemonDataStore(private val context: Context) {

    private val FAVORITES_KEY = stringSetPreferencesKey("favorites")

    // Get the list of favorite Pokémon names
    val favorites: Flow<Set<String>> = context.favoritePokemonDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            preferences[FAVORITES_KEY] ?: emptySet()
        }

    // Add a Pokémon to favorites
    suspend fun addFavorite(name: String) {
        context.favoritePokemonDataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITES_KEY] ?: emptySet()
            preferences[FAVORITES_KEY] = currentFavorites + name
        }
    }

    // Remove a Pokémon from favorites
    suspend fun removeFavorite(name: String) {
        context.favoritePokemonDataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITES_KEY] ?: emptySet()
            preferences[FAVORITES_KEY] = currentFavorites - name
        }
    }
}