package com.example.pokemonexplorerapp.base.screens.viewmodel

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.network.ApiSuccess
import com.example.domain.usecases.ManageFavoritesUseCase
import com.example.domain.usecases.PokemonUseCase
import com.example.pokemonexplorerapp.base.composables.DialogData
import com.example.pokemonexplorerapp.base.theme.CarminePink
import com.example.pokemonexplorerapp.utils.PokemonFilterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class Pokemon(
    val name: String,
    val types: List<PokemonFilterType>,
    val spriteUrl: String,
)

class FavoritesViewModel(
    private val manageFavoritesUseCase: ManageFavoritesUseCase,
    private val pokemonUseCase: PokemonUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _favoritePokemon = MutableStateFlow<List<Pokemon>>(emptyList())
    val favoritePokemon: StateFlow<List<Pokemon>> = _favoritePokemon

    private val _dialogData = MutableStateFlow<DialogData?>(null)
    val dialogData: StateFlow<DialogData?> = _dialogData

    init {
        loadFavoritePokemon()
    }

    private fun loadFavoritePokemon() {
        viewModelScope.launch {
            _isLoading.value = true
            manageFavoritesUseCase.favorites.collectLatest { favoriteNames ->
                val pokemonDetailsList = favoriteNames.mapNotNull { name ->
                    fetchPokemonDetails(name)
                }
                _favoritePokemon.value = pokemonDetailsList
                _isLoading.value = false
            }
        }
    }

    private suspend fun fetchPokemonDetails(name: String): Pokemon? {
        return when (val result = pokemonUseCase.getPokemonDetails(name)) {
            is ApiSuccess -> {
                val response = result.data ?: return null
                val pokemonName = response.name
                val pokemonTypes = response.types.mapNotNull { typeDetail ->
                    typeDetail.type.name.let { typeName ->
                        PokemonFilterType.entries.find {
                            it.name.equals(
                                typeName,
                                ignoreCase = true
                            )
                        }
                    }
                }
                if (pokemonTypes.isEmpty()) return null
                val pokemonSprite = response.sprites.frontDefault.orEmpty()
                Pokemon(
                    name = pokemonName,
                    types = pokemonTypes,
                    spriteUrl = pokemonSprite
                )
            }

            else -> null
        }
    }

    fun toggleFavorite(pokemonName: String, isFavorite: Boolean) {
        if (isFavorite) {
            // Show confirmation dialog
            _dialogData.value = DialogData.RemoveFavorite(
                annotatedString = buildAnnotatedString {
                    append("Do you want to remove ")
                    withStyle(SpanStyle(color = CarminePink)) {
                        append(pokemonName)
                    }
                    append(" from your favorites?")
                },
                primaryCallback = {
                    removeFavorite(pokemonName)
                    dismissDialog()
                },
                secondaryCallback = {
                    dismissDialog()
                }
            )
        } else {
            // Directly add to favorites
            addFavorite(pokemonName)
        }
    }

    private fun dismissDialog() {
        _dialogData.value = null
    }

    private fun addFavorite(pokemonName: String) {
        viewModelScope.launch {
            manageFavoritesUseCase.toggleFavorite(pokemonName, false)
        }
    }

    private fun removeFavorite(pokemonName: String) {
        viewModelScope.launch {
            manageFavoritesUseCase.toggleFavorite(pokemonName, true)
        }
    }
}