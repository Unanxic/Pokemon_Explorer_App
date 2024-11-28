package com.example.pokemonexplorerapp.base.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.ManageFavoritesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val manageFavoritesUseCase: ManageFavoritesUseCase
) : ViewModel() {

    val favoritePokemon: StateFlow<Set<String>> = manageFavoritesUseCase.favorites.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptySet()
    )

    fun toggleFavorite(name: String) {
        viewModelScope.launch {
            val isFavorite = favoritePokemon.value.contains(name)
            manageFavoritesUseCase.toggleFavorite(name, isFavorite)
        }
    }
}