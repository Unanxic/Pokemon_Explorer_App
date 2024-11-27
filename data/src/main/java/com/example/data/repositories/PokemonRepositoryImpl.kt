package com.example.data.repositories

import com.example.data.repositories.network.PokeApiService
import com.example.domain.models.PokemonResponse
import com.example.domain.models.network.ApiError
import com.example.domain.models.network.ApiErrorModel
import com.example.domain.models.network.ApiException
import com.example.domain.models.network.ApiNoInternetError
import com.example.domain.models.network.ApiSuccess
import com.example.domain.models.network.NetworkResult
import com.example.domain.repositories.PokemonRepository
import com.squareup.moshi.Moshi
import java.io.IOException

class PokemonRepositoryImpl(
    private val apiService: PokeApiService,
    private val moshi: Moshi
) : PokemonRepository {

    override suspend fun getPokemonDetails(name: String): NetworkResult<PokemonResponse> {
        return try {
            val response = apiService.getPokemonDetails(name)
            if (response.isSuccessful) {
                ApiSuccess(response.body())
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = errorBody?.let {
                    moshi.adapter(ApiErrorModel::class.java).fromJson(it)
                }
                ApiError(apiError)
            }
        } catch (e: IOException) {
            ApiNoInternetError()
        } catch (e: Exception) {
            ApiException(e)
        }
    }

    override suspend fun getPokemonList(limit: Int, offset: Int): NetworkResult<List<String>> {
        return try {
            val response = apiService.getPokemonList(limit, offset)
            if (response.isSuccessful) {
                val results = response.body()?.results?.map { it.name } ?: emptyList()
                ApiSuccess(results)
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = errorBody?.let {
                    moshi.adapter(ApiErrorModel::class.java).fromJson(it)
                }
                ApiError(apiError)
            }
        } catch (e: IOException) {
            ApiNoInternetError()
        } catch (e: Exception) {
            ApiException(e)
        }
    }
}