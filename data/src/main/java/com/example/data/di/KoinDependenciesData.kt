package com.example.data.di

import com.example.data.repositories.PokemonRepositoryImpl
import com.example.data.repositories.network.PokeApiService
import com.example.domain.repositories.PokemonRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val koinDependenciesData = module {
    // Moshi configuration
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    // OkHttpClient configuration
    single {
        OkHttpClient.Builder()
            .build()
    }

    // Repository implementation
    singleOf(::PokemonRepositoryImpl) bind PokemonRepository::class

    // Retrofit configuration
    single {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .build()
            .create(PokeApiService::class.java)
    }
}