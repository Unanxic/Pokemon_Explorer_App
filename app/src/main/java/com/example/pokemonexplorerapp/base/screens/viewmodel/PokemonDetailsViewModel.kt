package com.example.pokemonexplorerapp.base.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.network.ApiSuccess
import com.example.domain.usecases.PokemonUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PokemonDetails(
    val hp: Int,
    val attack: Int,
    val defense: Int
)


class PokemonDetailsViewModel(
    private val pokemonUseCase: PokemonUseCase
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _pokemonDetails = MutableStateFlow<PokemonDetails?>(null)
    val pokemonDetails: StateFlow<PokemonDetails?> get() = _pokemonDetails

    fun fetchPokemonDetails(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = pokemonUseCase.getPokemonDetails(name)
            if (result is ApiSuccess) {
                val response = result.data
                val hp = response?.stats?.find { it.stat.name.equals("hp", ignoreCase = true) }?.baseStat ?: 0
                val attack = response?.stats?.find { it.stat.name.equals("attack", ignoreCase = true) }?.baseStat ?: 0
                val defense = response?.stats?.find { it.stat.name.equals("defense", ignoreCase = true) }?.baseStat ?: 0

                _pokemonDetails.value = PokemonDetails(hp = hp, attack = attack, defense = defense)
            }
            _isLoading.value = false
        }
    }
}