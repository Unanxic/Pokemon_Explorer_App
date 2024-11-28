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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokemonexplorerapp.R
import com.example.pokemonexplorerapp.base.composables.AppScaffold
import com.example.pokemonexplorerapp.base.composables.PokemonCard
import com.example.pokemonexplorerapp.base.composables.TopBar
import com.example.pokemonexplorerapp.base.screens.viewmodel.FavoritesViewModel
import org.koin.compose.koinInject

@Composable
fun FavoritesScreen(
    paddingValues: PaddingValues,
    viewModel: FavoritesViewModel = koinInject(),
    navController: NavHostController
) {
    val favoritePokemon by viewModel.favoritePokemon.collectAsState()

    AppScaffold(
        topBar = {
            TopBar(title = "Favorites")
        }
    ) { paddingValues ->
        if (favoritePokemon.isEmpty()) {
            // Show placeholder if no favorites exist
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
                        contentDescription = "No Favorites Found",
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Favorites Found",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
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
                items(favoritePokemon.toList()) { pokemonName ->
                    PokemonCard(
                        name = pokemonName,
                        types = emptyList(), // Add proper types if needed
                        imageUrl = "", // Add a proper sprite URL if available
                        isFavorite = true,
                        onLikeClicked = { isLiked ->
                            if (!isLiked) viewModel.toggleFavorite(pokemonName)
                        }
                    )
                }
            }
        }
    }
}