package com.example.pokemonexplorerapp.base.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokemonexplorerapp.R
import com.example.pokemonexplorerapp.base.composables.AppDialog
import com.example.pokemonexplorerapp.base.composables.AppScaffold
import com.example.pokemonexplorerapp.base.composables.PokemonCard
import com.example.pokemonexplorerapp.base.composables.TopBar
import com.example.pokemonexplorerapp.base.navigation.Screen
import com.example.pokemonexplorerapp.base.screens.viewmodel.FavoritesViewModel
import com.example.pokemonexplorerapp.base.screens.viewmodel.Pokemon
import com.example.pokemonexplorerapp.base.theme.CarminePink
import com.example.pokemonexplorerapp.utils.addNavigationParams
import org.koin.compose.koinInject

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = koinInject(),
    navController: NavHostController
) {
    val favoritePokemon by viewModel.favoritePokemon.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val dialogData by viewModel.dialogData.collectAsState()

    AppScaffold(
        topBar = {
            TopBar(title = stringResource(R.string.favorites))
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CarminePink)
            }
        } else {
            if (favoritePokemon.isEmpty()) {
                EmptyFavoritesUI(paddingValues)
            } else {
                FavoritesList(
                    favoritePokemon = favoritePokemon,
                    paddingValues = paddingValues,
                    navController = navController,
                    onLikeClicked = { name, isFavorite ->
                        viewModel.toggleFavorite(name, isFavorite)
                    }
                )
            }
        }
        dialogData?.let { AppDialog(it) }
    }
}

@Composable
private fun FavoritesList(
    favoritePokemon: List<Pokemon>,
    paddingValues: PaddingValues,
    navController: NavHostController,
    onLikeClicked: (String, Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(favoritePokemon) { pokemon ->
            PokemonCard(
                name = pokemon.name,
                types = pokemon.types,
                imageUrl = pokemon.spriteUrl,
                isFavorite = true,
                onLikeClicked = onLikeClicked,
                onClick = {
                    navController.navigate(
                        Screen.PokemonDetails.route.addNavigationParams(
                            "name" to pokemon.name,
                            "types" to pokemon.types.joinToString(",") { it.name },
                            "imageUrl" to pokemon.spriteUrl
                        )
                    )
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun EmptyFavoritesUI(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.fish),
                contentDescription = stringResource(R.string.no_favorites_found),
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.no_favorites_found),
                color = Color.Gray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}