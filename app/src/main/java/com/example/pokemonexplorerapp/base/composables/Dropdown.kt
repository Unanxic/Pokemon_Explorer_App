package com.example.pokemonexplorerapp.base.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonexplorerapp.R
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
import com.example.pokemonexplorerapp.base.theme.animations.getDefaultTweenAnimationSpec
import com.example.pokemonexplorerapp.utils.setNoRippleClickable

enum class PokemonType(val displayName: String, val color: Color) {
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
@Composable
fun PokemonTypeDropdown(
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(PokemonType.All) }

    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 0f else 180f,
        animationSpec = getDefaultTweenAnimationSpec(),
        label = ""
    )

    val blackArrowAndTextTypes = setOf(
        PokemonType.All,
        PokemonType.Fire,
        PokemonType.Water,
        PokemonType.Grass,
        PokemonType.Electric,
        PokemonType.Psychic,
        PokemonType.Steel,
        PokemonType.Fairy
    )
    val isBlackTextAndArrow = selectedType in blackArrowAndTextTypes
    val arrowColorFilter = if (isBlackTextAndArrow) null else ColorFilter.tint(Color.White)
    val textColor = if (isBlackTextAndArrow) Color.Black else Color.White

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = selectedType.color,
                    shape = RoundedCornerShape(50.dp)
                )
                .setNoRippleClickable { expanded = !expanded }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedType.displayName,
                    fontSize = 16.sp,
                    color = textColor
                )
                Image(
                    painter = painterResource(id = R.drawable.dropdown_arrow_up),
                    contentDescription = null,
                    modifier = Modifier.rotate(arrowRotation),
                    colorFilter = arrowColorFilter
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            PokemonType.entries.forEach { type ->
                DropdownMenuItem(
                    onClick = {
                        selectedType = type
                        expanded = false
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(type.color, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = type.displayName, fontSize = 14.sp)
                        }
                    }
                )
            }
        }
    }
}