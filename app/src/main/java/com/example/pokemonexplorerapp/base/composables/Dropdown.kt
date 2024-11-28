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
import com.example.pokemonexplorerapp.base.theme.animations.getDefaultTweenAnimationSpec
import com.example.pokemonexplorerapp.utils.PokemonFilterType
import com.example.pokemonexplorerapp.utils.setNoRippleClickable


@Composable
fun PokemonTypeDropdown(
    modifier: Modifier = Modifier,
    onTypeSelected: (PokemonFilterType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedFilterType by remember { mutableStateOf(PokemonFilterType.All) }


    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 0f else 180f,
        animationSpec = getDefaultTweenAnimationSpec(),
        label = ""
    )

    val blackArrowAndTextTypes = setOf(
        PokemonFilterType.All,
        PokemonFilterType.Fire,
        PokemonFilterType.Water,
        PokemonFilterType.Grass,
        PokemonFilterType.Electric,
        PokemonFilterType.Psychic,
        PokemonFilterType.Steel,
        PokemonFilterType.Fairy
    )
    val isBlackTextAndArrow = selectedFilterType in blackArrowAndTextTypes
    val arrowColorFilter = if (isBlackTextAndArrow) null else ColorFilter.tint(Color.White)
    val textColor = if (isBlackTextAndArrow) Color.Black else Color.White

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = selectedFilterType.color,
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
                    text = selectedFilterType.displayName,
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
            PokemonFilterType.entries.forEach { filterType  ->
                DropdownMenuItem(
                    onClick = {
                        selectedFilterType = filterType
                        expanded = false
                        onTypeSelected(filterType)
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(filterType.color, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = filterType.displayName, fontSize = 14.sp)
                        }
                    }
                )
            }
        }
    }
}