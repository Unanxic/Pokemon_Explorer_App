package com.example.pokemonexplorerapp.di

import com.example.pokemonexplorerapp.base.screens.viewmodel.FavoritesViewModel
import com.example.pokemonexplorerapp.base.screens.viewmodel.HomeViewModel
import com.example.pokemonexplorerapp.base.screens.viewmodel.PokemonDetailsViewModel
import com.example.pokemonexplorerapp.utils.AppDialogManager
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val koinDependenciesApp = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::FavoritesViewModel)
    viewModelOf(::PokemonDetailsViewModel)
    singleOf(::AppDialogManager)
}