package com.example.dogbreedsgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.dogbreedsgallery.data.AppDependencies
import com.example.dogbreedsgallery.navigation.Route
import com.example.dogbreedsgallery.ui.breeds.BreedsScreen
import com.example.dogbreedsgallery.ui.breeds.BreedsViewModel
import com.example.dogbreedsgallery.ui.detail.BreedDetailScreen
import com.example.dogbreedsgallery.ui.detail.BreedDetailViewModel
import com.example.dogbreedsgallery.ui.favorites.FavoritesScreen
import com.example.dogbreedsgallery.ui.favorites.FavoritesViewModel
import com.example.dogbreedsgallery.ui.theme.DogBreedsGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppDependencies.init(applicationContext)

        setContent {
            DogBreedsGalleryTheme {
                val backStack = remember {
                    mutableStateListOf<Route>(Route.Breeds)
                }

                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator()
                    ),
                    entryProvider = { key ->
                        when (key) {
                            is Route.Breeds -> NavEntry(key) {
                                val vm: BreedsViewModel = viewModel(factory = viewModelFactory {
                                    initializer { BreedsViewModel(AppDependencies.repository) }
                                })
                                BreedsScreen(
                                    viewModel = vm,
                                    onBreedClick = { id -> backStack.add(Route.BreedDetail(id)) },
                                    onFavoritesClick = { backStack.add(Route.Favorites) }
                                )
                            }

                            is Route.Favorites -> NavEntry(key) {
                                val vm: FavoritesViewModel = viewModel(factory = viewModelFactory {
                                    initializer { FavoritesViewModel(AppDependencies.repository) }
                                })
                                FavoritesScreen(
                                    viewModel = vm,
                                    onBreedClick = { id -> backStack.add(Route.BreedDetail(id)) },
                                    onBack = { backStack.removeLastOrNull() }
                                )
                            }

                            is Route.BreedDetail -> NavEntry(key) {
                                val vm: BreedDetailViewModel = viewModel(
                                    key = "detail_${key.breedId}",
                                    factory = viewModelFactory {
                                        initializer {
                                            BreedDetailViewModel(AppDependencies.repository, key.breedId)
                                        }
                                    }
                                )
                                BreedDetailScreen(
                                    viewModel = vm,
                                    onBack = { backStack.removeLastOrNull() }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}