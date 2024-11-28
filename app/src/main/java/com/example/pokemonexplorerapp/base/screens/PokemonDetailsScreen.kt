package com.example.pokemonexplorerapp.base.screens


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.pokemonexplorerapp.base.composables.AppScaffold
import com.example.pokemonexplorerapp.base.composables.TopBar
import com.example.pokemonexplorerapp.base.theme.EarthYellow
import com.example.pokemonexplorerapp.base.theme.LightRed
import com.example.pokemonexplorerapp.base.theme.Verdigris
import com.example.pokemonexplorerapp.utils.PokemonType

@Composable
fun PokemonDetailsScreen(
    pokemonName: String,
    pokemonType: List<PokemonType>,
    imageUrl: String,
    navController: NavHostController
) {
    val primaryType = pokemonType.firstOrNull() ?: PokemonType.Fire
    AppScaffold(
        topBar = {
            TopBar(
                title = "Pokemon Details",
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                },
                showFavoriteIcon = true,
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Circular Background
                    Box(
                        modifier = Modifier
                            .size(150.dp) // Adjust size as needed
                            .background(primaryType.color.copy(alpha = 0.3f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Pokémon Image
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Pokemon Image",
                            modifier = Modifier
                                .size(120.dp), // Ensure the image is smaller than the circle
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = pokemonName,
                        color = Color.Black,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        pokemonType.forEach { type ->
                            PokemonTypeBadge(type = type)
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    PokemonBaseStats(
                        hp = 45,
                        attack = 49,
                        defense = 49
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 50.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(animDuration, animDelay), label = ""
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(Color.LightGray.copy(alpha = 0.5f))
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 14.sp
            )
            Text(
                text = (curPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun PokemonBaseStats(
    hp: Int,
    attack: Int,
    defense: Int,
    animDelayPerItem: Int = 100
) {
    val maxStatValue = 100

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Base Stats:",
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        PokemonStat(
            statName = "HP",
            statValue = hp,
            statMaxValue = maxStatValue,
            statColor = EarthYellow,
            animDelay = 0
        )
        Spacer(modifier = Modifier.height(35.dp))
        PokemonStat(
            statName = "Attack",
            statValue = attack,
            statMaxValue = maxStatValue,
            statColor = LightRed,
            animDelay = animDelayPerItem
        )
        Spacer(modifier = Modifier.height(35.dp))
        PokemonStat(
            statName = "Defense",
            statValue = defense,
            statMaxValue = maxStatValue,
            statColor = Verdigris,
            animDelay = animDelayPerItem * 2
        )
    }
}


@Composable
private fun PokemonTypeBadge(type: PokemonType) {
    val blackColor = setOf(
        PokemonType.Fire,
        PokemonType.Water,
        PokemonType.Grass,
        PokemonType.Electric,
        PokemonType.Psychic,
        PokemonType.Steel,
        PokemonType.Fairy
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
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}