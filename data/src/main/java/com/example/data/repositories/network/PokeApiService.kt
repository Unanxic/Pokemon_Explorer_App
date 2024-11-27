package com.example.data.repositories.network

import com.example.domain.models.PokemonResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(
        @Path("name") name: String
    ): Response<PokemonResponse>

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonListResponse>
}

@JsonClass(generateAdapter = true)
data class PokemonListResponse(
    @Json(name = "results") val results: List<PokemonListItem>
)

@JsonClass(generateAdapter = true)
data class PokemonListItem(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)