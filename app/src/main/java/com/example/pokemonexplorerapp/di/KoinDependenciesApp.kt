package com.example.pokemonexplorerapp.di

import com.example.pokemonexplorerapp.base.screens.viewmodel.FavoritesViewModel
import com.example.pokemonexplorerapp.base.screens.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val koinDependenciesApp = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::FavoritesViewModel)
}