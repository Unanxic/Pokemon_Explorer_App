package com.example.domain.repositories

import com.example.domain.models.PokemonResponse
import com.example.domain.models.network.NetworkResult

interface PokemonRepository {
    suspend fun getPokemonDetails(name: String): NetworkResult<PokemonResponse>
    suspend fun getPokemonList(limit: Int, offset: Int): NetworkResult<List<String>>
}