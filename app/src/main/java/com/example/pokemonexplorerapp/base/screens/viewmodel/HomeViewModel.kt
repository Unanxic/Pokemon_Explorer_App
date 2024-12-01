package com.example.pokemonexplorerapp.base.screens.viewmodel

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.network.ApiError
import com.example.domain.models.network.ApiException
import com.example.domain.models.network.ApiNoInternetError
import com.example.domain.models.network.ApiSuccess
import com.example.domain.usecases.ManageFavoritesUseCase
import com.example.domain.usecases.PokemonUseCase
import com.example.pokemonexplorerapp.base.composables.DialogData
import com.example.pokemonexplorerapp.base.theme.CarminePink
import com.example.pokemonexplorerapp.utils.PokemonFilterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PokemonCardUI(
    val name: String,
    val types: List<PokemonFilterType>,
    val spriteUrl: String
)

class
HomeViewModel(
    private val pokemonUseCase: PokemonUseCase,
    private val manageFavoritesUseCase: ManageFavoritesUseCase,
) : ViewModel() {

    private val _allPokemonNames =
        MutableStateFlow<List<String>>(emptyList())
    private val _allPokemonDetails =
        MutableStateFlow<List<PokemonCardUI>>(emptyList())

    private val _pokemonList = MutableStateFlow<List<PokemonCardUI>>(emptyList())
    val pokemonList: StateFlow<List<PokemonCardUI>> = _pokemonList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _hasError = MutableStateFlow<String?>(null)
    val hasError: StateFlow<String?> = _hasError

    private val _searchTerm = MutableStateFlow("")
    val searchTerm: StateFlow<String> = _searchTerm

    private val _selectedFilterType = MutableStateFlow(PokemonFilterType.All)

    private val _dialogData = MutableStateFlow<DialogData?>(null)
    val dialogData: StateFlow<DialogData?> = _dialogData

    private var offset = 0
    private val limit = 10

    init {
        viewModelScope.launch {
            fetchAllPokemonNames()
            fetchInitialPokemonBatch()
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

    fun toggleFavorite(pokemonName: String, isFavorite: Boolean) {
        if (isFavorite) {
            // Show RemoveFavorite confirmation dialog
            _dialogData.value = DialogData.RemoveFavorite(
                annotatedString = buildAnnotatedString {
                    append("Do you want to remove ")
                    withStyle(SpanStyle(color = CarminePink)) {
                        append(pokemonName.replaceFirstChar { it.uppercase() })
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
            // Show AddFavorite confirmation dialog
            _dialogData.value = DialogData.AddFavorite(
                annotatedString = buildAnnotatedString {
                    append("You added ")
                    withStyle(SpanStyle(color = CarminePink)) {
                        append(pokemonName.replaceFirstChar { it.uppercase() })
                    }
                    append(" to your Favorites list!")
                },
                primaryCallback = {
                    addFavorite(pokemonName)
                    dismissDialog()
                }
            )
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

    private fun searchPokemonByName(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Check if the Pokémon is already loaded
                val existingPokemon = _allPokemonDetails.value.find { it.name.equals(name, ignoreCase = true) }
                if (existingPokemon != null) {
                    _pokemonList.value = listOf(existingPokemon)
                } else {
                    // Fetch details for the searched Pokémon
                    val pokemonDetails = fetchPokemonDetails(name)
                    if (pokemonDetails != null) {
                        _pokemonList.value = listOf(pokemonDetails)
                        _allPokemonDetails.value += pokemonDetails
                    } else {
                        _pokemonList.value = emptyList()
                    }
                }
            } catch (e: Exception) {
                _hasError.value = "Failed to search Pokémon."
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun fetchAllPokemonNames() {
        _isLoading.value = true
        try {
            when (val result = pokemonUseCase.getPokemonList(limit = Int.MAX_VALUE, offset = 0)) {
                is ApiSuccess -> {
                    _allPokemonNames.value = result.data.orEmpty()
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
            _isLoading.value = true
            try {
                _selectedFilterType.emit(filterType)

                val filteredPokemons = _allPokemonDetails.value.filter { pokemon ->
                    filterType == PokemonFilterType.All || pokemon.types.any { it == filterType }
                }

                if (filteredPokemons.isEmpty()) {
                    _hasError.value = "No Pokémon found for the selected category."
                }

                _pokemonList.value = filteredPokemons
            } catch (e: Exception) {
                _hasError.value = "Failed to load Pokémon for the selected type."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSearchTerm(term: String) {
        viewModelScope.launch {
            _searchTerm.emit(term)
            if (term.isNotEmpty()) {
                searchPokemonByName(term)
            } else {
                _pokemonList.value = _allPokemonDetails.value
            }
        }
    }

    private suspend fun fetchInitialPokemonBatch() {
        _isLoading.value = true
        try {
            val firstBatch = _allPokemonNames.value.take(limit).mapNotNull { fetchPokemonDetails(it) }
            _pokemonList.value = firstBatch
            _allPokemonDetails.value = firstBatch
            offset = firstBatch.size
        } catch (e: Exception) {
            _hasError.value = "Failed to fetch initial Pokémon batch."
        } finally {
            _isLoading.value = false
        }
    }


    private suspend fun fetchPokemonDetails(name: String): PokemonCardUI? {
        return when (val result = pokemonUseCase.getPokemonDetails(name)) {
            is ApiSuccess -> {
                val data = result.data
                if (data != null) {
                    val types = data.types.mapNotNull { mapToPokemonType(it.type.name) }
                    if (types.isNotEmpty()) {
                        PokemonCardUI(
                            name = data.name,
                            types = types,
                            spriteUrl = data.sprites.frontDefault ?: ""
                        )
                    } else null
                } else null
            }
            else -> null
        }
    }

    private fun mapToPokemonType(typeName: String): PokemonFilterType? {
        return when (typeName.lowercase()) {
            "grass" -> PokemonFilterType.Grass
            "steel" -> PokemonFilterType.Steel
            "fire" -> PokemonFilterType.Fire
            "water" -> PokemonFilterType.Water
            "electric" -> PokemonFilterType.Electric
            "dragon" -> PokemonFilterType.Dragon
            "psychic" -> PokemonFilterType.Psychic
            "ghost" -> PokemonFilterType.Ghost
            "dark" -> PokemonFilterType.Dark
            "fairy" -> PokemonFilterType.Fairy
            else -> null
        }
    }
}