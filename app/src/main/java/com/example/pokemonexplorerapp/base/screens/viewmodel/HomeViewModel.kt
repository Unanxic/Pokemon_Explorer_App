package com.example.pokemonexplorerapp.base.screens.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.network.ApiError
import com.example.domain.models.network.ApiException
import com.example.domain.models.network.ApiNoInternetError
import com.example.domain.models.network.ApiSuccess
import com.example.domain.usecases.ManageFavoritesUseCase
import com.example.domain.usecases.PokemonUseCase
import com.example.pokemonexplorerapp.utils.PokemonFilterType
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
    private val pokemonUseCase: PokemonUseCase,
    private val manageFavoritesUseCase: ManageFavoritesUseCase
) : ViewModel() {

    private val _allPokemonNames =
        MutableStateFlow<List<String>>(emptyList()) // Pokémon names for search
    private val _allPokemonDetails =
        MutableStateFlow<List<PokemonCardUI>>(emptyList()) // Cached Pokémon details

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
    private val _selectedFilterType = MutableStateFlow(PokemonFilterType.All)


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

    val favorites: StateFlow<Set<String>> = manageFavoritesUseCase.favorites.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptySet()
    )

    val filteredPokemonList = combine(_allPokemonDetails, _searchTerm, _selectedFilterType) { allDetails, search, filterType ->
        val searchLower = search.lowercase()

        allDetails.filter { pokemon ->
            val hasValidTypes = pokemon.types.isNotEmpty()
            val matchesSearch = pokemon.name.lowercase().contains(searchLower)
            val matchesType = filterType == PokemonFilterType.All || pokemon.types.any { it.displayName == filterType.displayName }
            hasValidTypes && matchesSearch && matchesType
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun toggleFavorite(name: String) {
        viewModelScope.launch {
            val isFavorite = favorites.value.contains(name)
            manageFavoritesUseCase.toggleFavorite(name, isFavorite)
        }
    }

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

    fun updateFilterTypeWithLoading(filterType: PokemonFilterType) {
        viewModelScope.launch {
            _isLoading.value = true // Show loader
            try {
                _selectedFilterType.emit(filterType) // Update filter type
                val filteredBatch = _allPokemonNames.value
                    .mapNotNull { fetchPokemonDetails(it) } // Filter Pokémon by type

                // Filter only the Pokémon that match the selected type
                val filteredPokemons = filteredBatch.filter { pokemon ->
                    filterType == PokemonFilterType.All || pokemon.types.any { it.displayName == filterType.displayName }
                }
                _pokemonList.value = filteredPokemons
            } catch (e: Exception) {
                _hasError.value = "Failed to load Pokémon for the selected type."
            } finally {
                _isLoading.value = false // Hide loader
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
            offset = firstBatch.size // Keep track of how many Pokémon have been loaded
        } catch (e: Exception) {
            _hasError.value = "Failed to fetch initial Pokémon batch."
        } finally {
            _isLoading.value = false
        }
    }

    fun fetchPokemonList() {
        if (_isLoading.value || _isLoadingMore.value || !hasMorePokemon.value) return

        _isLoadingMore.value = true // Always show the loading state when "Load More" is clicked
        _hasError.value = null // Reset any previous errors

        viewModelScope.launch {
            try {
                // Fetch the next batch of Pokémon
                val nextBatch = _allPokemonNames.value.drop(offset).take(limit)
                    .mapNotNull { fetchPokemonDetails(it) }

                // Append the new batch to the existing list
                _pokemonList.value += nextBatch
                _allPokemonDetails.value += nextBatch
                offset += nextBatch.size

                // Check if there are more Pokémon to load
                if (nextBatch.size < limit) {
                    hasMorePokemon.value = false
                }
            } catch (e: Exception) {
                _hasError.value = "Failed to load more Pokémon."
            } finally {
                _isLoadingMore.value = false
            }
        }
    }


    private suspend fun fetchPokemonDetails(name: String): PokemonCardUI? {
        return when (val result = pokemonUseCase.getPokemonDetails(name)) {
            is ApiSuccess -> {
                val data = result.data
                if (data != null) {
                    val types = data.types.mapNotNull { mapToPokemonType(it.type.name) }
                    if (types.isNotEmpty()) { // Only include Pokémon with valid types
                        PokemonCardUI(
                            name = data.name,
                            types = types,
                            spriteUrl = data.sprites.frontDefault ?: ""
                        )
                    } else null // Exclude Pokémon with no valid types
                } else null
            }
            else -> null
        }
    }

    private fun mapToPokemonType(typeName: String): PokemonType? {
        return when (typeName.lowercase()) {
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
            else -> null // Return null for unknown types
        }
    }
}