package com.example.pokemonexplorerapp.base.screens.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.network.ApiError
import com.example.domain.models.network.ApiException
import com.example.domain.models.network.ApiNoInternetError
import com.example.domain.models.network.ApiSuccess
import com.example.domain.usecases.PokemonUseCase
import com.example.pokemonexplorerapp.utils.PokemonType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PokemonCardUI(
    val name: String,
    val types: List<PokemonType>,
    val spriteUrl: String
)

class HomeViewModel(
    private val pokemonUseCase: PokemonUseCase
) : ViewModel() {

    private val _allPokemonNames = MutableStateFlow<List<String>>(emptyList()) // Pokémon names for search
    private val _allPokemonDetails = MutableStateFlow<List<PokemonCardUI>>(emptyList()) // Cached Pokémon details

    private val _pokemonList = MutableStateFlow<List<PokemonCardUI>>(emptyList())
    val pokemonList: StateFlow<List<PokemonCardUI>> get() = _pokemonList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> get() = _isLoadingMore

    private val _hasError = MutableStateFlow<String?>(null)
    val hasError: StateFlow<String?> get() = _hasError

    private val _searchTerm = MutableStateFlow("")
    val searchTerm: StateFlow<String> get() = _searchTerm

    var hasMorePokemon = mutableStateOf(true)
        private set

    private var offset = 0
    private val limit = 10

    init {
        viewModelScope.launch {
            fetchAllPokemonNames() // Fetch Pokémon names
            fetchInitialPokemonBatch() // Fetch and display the first 10 Pokémon immediately
        }
    }

    val filteredPokemonList = combine(_allPokemonDetails, _searchTerm) { allDetails, search ->
        if (search.isNotEmpty()) {
            val searchLower = search.lowercase()
            allDetails.filter { it.name.lowercase().contains(searchLower) }
        } else {
            _pokemonList.value
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private suspend fun fetchAllPokemonNames() {
        _isLoading.value = true
        try {
            when (val result = pokemonUseCase.getPokemonList(limit = Int.MAX_VALUE, offset = 0)) {
                is ApiSuccess -> {
                    _allPokemonNames.value = result.data.orEmpty() // Cache Pokémon names
                }
                is ApiError -> _hasError.value = "Failed to fetch Pokémon names."
                is ApiNoInternetError -> _hasError.value = "No internet connection."
                is ApiException -> _hasError.value = "An error occurred."
                else -> {}
            }
        } finally {
            _isLoading.value = false
        }
        fetchAllPokemonDetailsInBackground()
    }

    private fun fetchAllPokemonDetailsInBackground() {
        viewModelScope.launch {
            _allPokemonNames.value.forEach { name ->
                if (_allPokemonDetails.value.none { it.name == name }) {
                    fetchPokemonDetails(name)?.let { detail ->
                        _allPokemonDetails.value += detail
                    }
                }
            }
        }
    }

    fun updateSearchTerm(term: String) {
        viewModelScope.launch {
            _searchTerm.emit(term)
        }
    }

    private suspend fun fetchInitialPokemonBatch() {
        _isLoading.value = true
        try {
            val firstBatch = _allPokemonNames.value.take(limit).mapNotNull { fetchPokemonDetails(it) }
            _pokemonList.value = firstBatch
            _allPokemonDetails.value = firstBatch
            offset = firstBatch.size
        } finally {
            _isLoading.value = false
        }
    }

    fun fetchPokemonList() {
        if (_isLoading.value || _isLoadingMore.value || !hasMorePokemon.value) return

        val isLoadMore = _pokemonList.value.isNotEmpty()

        if (isLoadMore) {
            _isLoadingMore.value = true
        } else {
            _isLoading.value = true
        }

        _hasError.value = null

        viewModelScope.launch {
            try {
                val nextBatch = _allPokemonNames.value.drop(offset).take(limit).mapNotNull { fetchPokemonDetails(it) }
                _pokemonList.value += nextBatch
                _allPokemonDetails.value += nextBatch
                offset += nextBatch.size

                if (nextBatch.size < limit) {
                    hasMorePokemon.value = false
                }
            } finally {
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
                    PokemonCardUI(
                        name = data.name,
                        types = data.types.map { mapToPokemonType(it.type.name) },
                        spriteUrl = data.sprites.frontDefault ?: ""
                    )
                } else null
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