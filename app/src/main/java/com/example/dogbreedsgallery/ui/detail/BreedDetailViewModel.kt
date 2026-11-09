package com.example.dogbreedsgallery.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedsgallery.data.BreedRepository
import com.example.dogbreedsgallery.data.local.BreedEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BreedDetailViewModel(
    private val repository: BreedRepository,
    private val breedId: Int
) : ViewModel() {

    private val _breed = MutableStateFlow<BreedEntity?>(null)
    val breed: StateFlow<BreedEntity?> = _breed.asStateFlow()

    private val _images = MutableStateFlow<List<String>>(emptyList())
    val images: StateFlow<List<String>> = _images.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _isLoading.value = true
            _breed.value = repository.allBreeds.first().firstOrNull { it.id == breedId }
            _images.value = repository.getImagesForBreed(breedId)
            _isLoading.value = false
        }
    }

    fun toggleFavorite() {
        val current = _breed.value ?: return
        viewModelScope.launch {
            repository.toggleFavorite(current.id, current.isFavorite)
            _breed.value = current.copy(isFavorite = !current.isFavorite)
        }
    }
}