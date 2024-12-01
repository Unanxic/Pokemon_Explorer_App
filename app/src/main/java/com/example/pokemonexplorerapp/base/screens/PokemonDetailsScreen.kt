package com.example.pokemonexplorerapp.base.screens


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.pokemonexplorerapp.R
import com.example.pokemonexplorerapp.base.composables.AppScaffold
import com.example.pokemonexplorerapp.base.composables.PokemonTypeBadge
import com.example.pokemonexplorerapp.base.composables.TopBar
import com.example.pokemonexplorerapp.base.screens.viewmodel.PokemonDetails
import com.example.pokemonexplorerapp.base.screens.viewmodel.PokemonDetailsViewModel
import com.example.pokemonexplorerapp.base.theme.CarminePink
import com.example.pokemonexplorerapp.base.theme.EarthYellow
import com.example.pokemonexplorerapp.base.theme.LightRed
import com.example.pokemonexplorerapp.base.theme.Verdigris
import com.example.pokemonexplorerapp.utils.PokemonFilterType
import org.koin.compose.koinInject

@Composable
fun PokemonDetailsScreen(
    pokemonName: String,
    pokemonType: List<PokemonFilterType>,
    imageUrl: String,
    navController: NavHostController,
    viewModel: PokemonDetailsViewModel = koinInject()
) {
    val pokemonDetails by viewModel.pokemonDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(pokemonName) {
        viewModel.fetchPokemonDetails(pokemonName)
    }

    val primaryType = pokemonType.firstOrNull()

    AppScaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.pokemon_details),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = CarminePink)
                }
            } else {
                PokemonDetailsContent(
                    pokemonName = pokemonName,
                    pokemonType = pokemonType,
                    imageUrl = imageUrl,
                    primaryType = primaryType,
                    pokemonDetails = pokemonDetails,
                    paddingValues = paddingValues
                )
            }
        }
    }
}

@Composable
private fun PokemonDetailsContent(
    pokemonName: String,
    pokemonType: List<PokemonFilterType>,
    imageUrl: String,
    primaryType: PokemonFilterType?,
    pokemonDetails: PokemonDetails?,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        PokemonImageHeader(imageUrl = imageUrl, primaryType = primaryType)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            PokemonNameAndType(pokemonName = pokemonName, pokemonType = pokemonType)
            Spacer(modifier = Modifier.height(40.dp))
            pokemonDetails?.let {
                PokemonStatsSection(
                    hp = it.hp,
                    attack = it.attack,
                    defense = it.defense
                )
            }
        }
    }
}

@Composable
private fun PokemonStatsSection(hp: Int, attack: Int, defense: Int) {
    PokemonBaseStats(
        hp = hp,
        attack = attack,
        defense = defense
    )
}

@Composable
private fun PokemonNameAndType(pokemonName: String, pokemonType: List<PokemonFilterType>) {
    Text(
        text = pokemonName.replaceFirstChar { it.uppercase() },
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
            PokemonTypeBadge(type = type, fontSize = 22.sp)
        }
    }
}

@Composable
private fun PokemonImageHeader(imageUrl: String, primaryType: PokemonFilterType?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(
                    primaryType?.color?.copy(alpha = 0.3f) ?: Color.Gray,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Pokemon Image",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}



@Composable
private fun PokemonStat(
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
private fun PokemonBaseStats(
    hp: Int,
    attack: Int,
    defense: Int,
    animDelayPerItem: Int = 100
) {
    val maxStatValue = listOf(hp, attack, defense)
        .maxOrNull()
        ?.let { stat ->
            when {
                stat > 200 -> 300
                stat > 100 -> 200
                else -> 100
            }
        } ?: 100

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.base_stats),
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        PokemonStat(
            statName = stringResource(R.string.hp),
            statValue = hp,
            statMaxValue = maxStatValue,
            statColor = EarthYellow,
            animDelay = 0
        )
        Spacer(modifier = Modifier.height(35.dp))
        PokemonStat(
            statName = stringResource(R.string.attack),
            statValue = attack,
            statMaxValue = maxStatValue,
            statColor = LightRed,
            animDelay = animDelayPerItem
        )
        Spacer(modifier = Modifier.height(35.dp))
        PokemonStat(
            statName = stringResource(R.string.defense),
            statValue = defense,
            statMaxValue = maxStatValue,
            statColor = Verdigris,
            animDelay = animDelayPerItem * 2
        )
    }
}