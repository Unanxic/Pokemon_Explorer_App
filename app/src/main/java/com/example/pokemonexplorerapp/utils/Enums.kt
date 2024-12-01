package com.example.pokemonexplorerapp.utils

import androidx.compose.ui.graphics.Color
import com.example.pokemonexplorerapp.base.theme.AmethystPurple
import com.example.pokemonexplorerapp.base.theme.AppleGreen
import com.example.pokemonexplorerapp.base.theme.BlueEyes
import com.example.pokemonexplorerapp.base.theme.Dune
import com.example.pokemonexplorerapp.base.theme.HavelockBlue
import com.example.pokemonexplorerapp.base.theme.LightAzure
import com.example.pokemonexplorerapp.base.theme.LightFuchsiaPink
import com.example.pokemonexplorerapp.base.theme.MidnightBlue
import com.example.pokemonexplorerapp.base.theme.PastelOrange
import com.example.pokemonexplorerapp.base.theme.PetrolBlue
import com.example.pokemonexplorerapp.base.theme.Sandstorm

enum class PokemonFilterType(val displayName: String, val color: Color) {
    All("All", LightAzure),
    Fire("Fire", PastelOrange),
    Water("Water", HavelockBlue),
    Grass("Grass", AppleGreen),
    Electric("Electric", Sandstorm),
    Dragon("Dragon", BlueEyes),
    Psychic("Psychic", LightFuchsiaPink),
    Ghost("Ghost", MidnightBlue),
    Dark("Dark", Dune),
    Steel("Steel", PetrolBlue),
    Fairy("Fairy", AmethystPurple)
}
