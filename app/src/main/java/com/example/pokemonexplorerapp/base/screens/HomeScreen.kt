package com.example.pokemonexplorerapp.base.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokemonexplorerapp.R
import com.example.pokemonexplorerapp.base.composables.AppScaffold
import com.example.pokemonexplorerapp.base.composables.GenericOutlinedTextField
import com.example.pokemonexplorerapp.base.composables.PokemonCard
import com.example.pokemonexplorerapp.base.composables.PokemonTypeDropdown
import com.example.pokemonexplorerapp.base.composables.TopBar
import com.example.pokemonexplorerapp.base.screens.viewmodel.HomeViewModel
import com.example.pokemonexplorerapp.base.theme.CarminePink
import com.example.pokemonexplorerapp.base.theme.MidnightBlue
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues,
    viewModel: HomeViewModel = koinInject()
) {

    val pokemonList by viewModel.pokemonList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val hasError by viewModel.hasError.collectAsState()
    val hasMorePokemon by viewModel.hasMorePokemon
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()

    val filteredPokemonList by viewModel.filteredPokemonList.collectAsState()
    val searchTerm by viewModel.searchTerm.collectAsState()

    val favorites by viewModel.favorites.collectAsState() // Observe favorites


    AppScaffold(
        topBar = {
            TopBar(
                title = "Home",
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Content Layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                    )
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                GenericOutlinedTextField(
                    initialValue = searchTerm,
                    onValueChanged = viewModel::updateSearchTerm,
                    placeHolder = "e.g., Pikachu",
                    imeAction = ImeAction.Done,
                    trailingIconComposable = {
                        Image(
                            painter = painterResource(id = R.drawable.search_icon),
                            contentDescription = "Search Icon"
                        )
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
                PokemonTypeDropdown(
                    modifier = Modifier.width(200.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // LazyColumn for Pokémon Cards
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding()),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredPokemonList) { pokemon ->
                        PokemonCard(
                            name = pokemon.name,
                            types = pokemon.types,
                            imageUrl = pokemon.spriteUrl,
                            isFavorite = favorites.contains(pokemon.name),
                            onLikeClicked = { isLiked -> viewModel.toggleFavorite(pokemon.name) }
                        )
                    }
                    // Load More Button or Loader
                    item {
                        if (searchTerm.isEmpty() && pokemonList.isNotEmpty()) {
                                LoadMoreButtonOrLoader(
                                    isLoading = isLoadingMore,
                                    onLoadMore = { viewModel.fetchPokemonList() }
                                )
                        }
                    }
                    // Bottom Spacer
                    item {
                        Spacer(modifier = Modifier.height(90.dp))
                    }
                }
            }
            // CircularProgressIndicator in the middle of the screen
            if (isLoading && pokemonList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CarminePink)
                }
            }
        }
    }
}

@Composable
fun LoadMoreButtonOrLoader(
    isLoading: Boolean,
    onLoadMore: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 100.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MidnightBlue,
                strokeWidth = 2.dp,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Button(
                onClick = { onLoadMore() },
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 3.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Text(
                    text = "Load More",
                    color = Color.Black,
                    fontSize = 15.sp
                )
            }
        }
    }
}
