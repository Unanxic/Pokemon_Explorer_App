package com.example.domain.usecases

import com.example.domain.models.PokemonResponse
import com.example.domain.models.network.NetworkResult
import com.example.domain.repositories.PokemonRepository

class PokemonUseCase(
    private val repository: PokemonRepository
) {

    /**
     * Fetch detailed information about a Pokémon by its name.
     * @param name The name of the Pokémon.
     * @return A [NetworkResult] containing the Pokémon details.
     */
    suspend fun getPokemonDetails(name: String): NetworkResult<PokemonResponse> {
        return repository.getPokemonDetails(name)
    }

    /**
     * Fetch a list of Pokémon names with pagination.
     * @param limit The number of Pokémon to retrieve.
     * @param offset The starting index for pagination.
     * @return A [NetworkResult] containing the list of Pokémon names.
     */
    suspend fun getPokemonList(limit: Int, offset: Int): NetworkResult<List<String>> {
        return repository.getPokemonList(limit, offset)
    }
}