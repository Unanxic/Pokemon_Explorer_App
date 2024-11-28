package com.example.domain.di

import com.example.domain.usecases.ManageFavoritesUseCase
import com.example.domain.usecases.PokemonUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val koinDependenciesDomain = module {
    singleOf(::PokemonUseCase)
    singleOf(::ManageFavoritesUseCase)
}