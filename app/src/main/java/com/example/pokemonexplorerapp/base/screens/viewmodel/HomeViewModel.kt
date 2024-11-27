package com.example.pokemonexplorerapp.base.screens.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.network.ApiError
import com.example.domain.models.network.ApiException
import com.example.domain.models.network.ApiLoading
import com.example.domain.models.network.ApiNoInternetError
import com.example.domain.models.network.ApiSuccess
import com.example.domain.usecases.PokemonUseCase
import com.example.pokemonexplorerapp.utils.PokemonType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PokemonCardUI(
    val name: String,
    val types: List<PokemonType>,
    val spriteUrl: String
)

class HomeViewModel(
    private val pokemonUseCase: PokemonUseCase
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<List<PokemonCardUI>>(emptyList())
    val pokemonList: StateFlow<List<PokemonCardUI>> get() = _pokemonList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> get() = _isLoadingMore

    private val _hasError = MutableStateFlow<String?>(null)
    val hasError: StateFlow<String?> get() = _hasError

    var hasMorePokemon = mutableStateOf(true)
        private set

    private var offset = 0
    private val limit = 10

    init {
        fetchPokemonList()
    }

    fun fetchPokemonList() {
        // Avoid fetching if already loading or if there are no more Pokémon to load
        if (_isLoading.value || _isLoadingMore.value || !hasMorePokemon.value) return

        val isLoadMore = pokemonList.value.isNotEmpty()

        // Set the appropriate loading state
        if (isLoadMore) {
            _isLoadingMore.value = true
        } else {
            _isLoading.value = true
        }

        _hasError.value = null

        // Launch in a coroutine
        viewModelScope.launch {
            try {
                when (val result = pokemonUseCase.getPokemonList(limit, offset)) {
                    is ApiSuccess -> {
                        val newPokemon = result.data?.mapNotNull { name ->
                            fetchPokemonDetails(name)
                        }.orEmpty()
                        // Update the Pokémon list
                        _pokemonList.value += newPokemon

                        // Update offset for pagination
                        offset += limit

                        // Check if there are no more Pokémon to load
                        if (newPokemon.isEmpty() || newPokemon.size < limit) {
                            hasMorePokemon.value = false
                        }
                    }
                    is ApiError -> TODO()
                    is ApiNoInternetError -> TODO()
                    is ApiException -> TODO()

                    is ApiLoading -> TODO()
                }
            } finally {
                // Ensure loading states are reset
                if (isLoadMore) {
                    _isLoadingMore.value = false
                } else {
                    _isLoading.value = false
                }
            }
        }
    }


    private suspend fun fetchPokemonDetails(name: String): PokemonCardUI? {
        return when (val result = pokemonUseCase.getPokemonDetails(name)) {
            is ApiSuccess -> {
                val data = result.data
                if (data != null) {
                    val types = data.types.map { mapToPokemonType(it.type.name) }
                    val spriteUrl = data.sprites.frontDefault ?: ""
                    PokemonCardUI(
                        name = data.name,
                        types = types,
                        spriteUrl = spriteUrl
                    )
                } else {
                    null
                }
            }
            else -> null
        }
    }

    private fun mapToPokemonType(typeName: String): PokemonType {
        return when (typeName) {
            "grass" -> PokemonType.Grass
            "steel" -> PokemonType.Steel
            "fire" -> PokemonType.Fire
            "water" -> PokemonType.Water
            "electric" -> PokemonType.Electric
            "dragon" -> PokemonType.Dragon
            "psychic" -> PokemonType.Psychic
            "ghost" -> PokemonType.Ghost
            "dark" -> PokemonType.Dark
            "fairy" -> PokemonType.Fairy
            else -> PokemonType.All
        }
    }
}