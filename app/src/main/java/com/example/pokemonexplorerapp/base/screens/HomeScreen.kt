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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokemonexplorerapp.R
import com.example.pokemonexplorerapp.base.composables.AppDialog
import com.example.pokemonexplorerapp.base.composables.AppScaffold
import com.example.pokemonexplorerapp.base.composables.GenericOutlinedTextField
import com.example.pokemonexplorerapp.base.composables.PokemonCard
import com.example.pokemonexplorerapp.base.composables.PokemonTypeDropdown
import com.example.pokemonexplorerapp.base.composables.TopBar
import com.example.pokemonexplorerapp.base.navigation.Screen
import com.example.pokemonexplorerapp.base.screens.viewmodel.HomeViewModel
import com.example.pokemonexplorerapp.base.screens.viewmodel.PokemonCardUI
import com.example.pokemonexplorerapp.base.theme.CarminePink
import com.example.pokemonexplorerapp.utils.addNavigationParams
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = koinInject()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val filteredPokemonList by viewModel.filteredPokemonList.collectAsState()
    val searchTerm by viewModel.searchTerm.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val dialogData by viewModel.dialogData.collectAsState()

    AppScaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.home),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
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
                    onValueChanged = { query ->
                        viewModel.updateSearchTerm(query)
                    },
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
                    modifier = Modifier.width(200.dp),
                    onTypeSelected = viewModel::updateFilterTypeWithLoading
                )

                Spacer(modifier = Modifier.height(10.dp))

                PokemonListContent(
                    isLoading = isLoading,
                    pokemonList = filteredPokemonList,
                    favorites = favorites,
                    paddingValues = paddingValues,
                    onLikeClicked = { name, isFavorite ->
                        viewModel.toggleFavorite(name, isFavorite)
                    },
                    onPokemonClicked = { pokemon ->
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
        }
        dialogData?.let { AppDialog(it) }
    }
}

@Composable
fun PokemonListContent(
    isLoading: Boolean,
    pokemonList: List<PokemonCardUI>,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    onLikeClicked: (String, Boolean) -> Unit,
    onPokemonClicked: (PokemonCardUI) -> Unit
) {
    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CarminePink)
            }
        }
        pokemonList.isEmpty() -> {
            EmptyListUI(paddingValues = paddingValues)
        }
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pokemonList) { pokemon ->
                    PokemonCard(
                        name = pokemon.name,
                        types = pokemon.types,
                        imageUrl = pokemon.spriteUrl,
                        isFavorite = favorites.contains(pokemon.name),
                        onLikeClicked = onLikeClicked,
                        onClick = { onPokemonClicked(pokemon) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
private fun EmptyListUI(paddingValues: PaddingValues) {
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
                contentDescription = stringResource(R.string.no_pokemon_found),
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.no_pokemon_found),
                color = Color.Gray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
