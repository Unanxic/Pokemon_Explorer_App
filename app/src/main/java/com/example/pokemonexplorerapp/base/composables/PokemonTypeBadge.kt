package com.example.pokemonexplorerapp.base.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonexplorerapp.utils.PokemonFilterType

@Composable
fun PokemonTypeBadge(
    type: PokemonFilterType,
    fontSize: TextUnit = 12.sp
) {
    val blackColor = setOf(
        PokemonFilterType.Fire,
        PokemonFilterType.Water,
        PokemonFilterType.Grass,
        PokemonFilterType.Electric,
        PokemonFilterType.Psychic,
        PokemonFilterType.Steel,
        PokemonFilterType.Fairy
    )
    val textColor = if (type in blackColor) Color.Black else Color.White
    Box(
        modifier = Modifier
            .background(type.color, shape = RoundedCornerShape(15.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = type.displayName,
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}