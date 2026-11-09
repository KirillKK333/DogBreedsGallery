package com.example.dogbreedsgallery.ui.breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedsgallery.data.BreedRepository
import com.example.dogbreedsgallery.data.local.BreedEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BreedsUiState(
    val breeds: List<BreedEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

class BreedsViewModel(
    private val repository: BreedRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _isRefreshing = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<BreedsUiState> = combine(
        repository.allBreeds,
        _searchQuery,
        _isRefreshing,
        _error,
        _isLoading
    ) { breeds, query, refreshing, error, loading ->
        val filtered = if (query.isBlank()) breeds
        else breeds.filter { it.name.contains(query, ignoreCase = true) }
        BreedsUiState(
            breeds = filtered,
            isLoading = loading,
            isRefreshing = refreshing,
            error = error,
            searchQuery = query
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BreedsUiState()
    )

    init {
        refresh()
    }

    fun onSearchQueryChange(q: String) { _searchQuery.value = q }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _error.value = null
            val result = repository.refreshBreeds()
            if (result.isFailure) {
                _error.value = result.exceptionOrNull()?.message ?: "Ошибка загрузки"
            }
            _isRefreshing.value = false
        }
    }

    fun toggleFavorite(breed: BreedEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(breed.id, breed.isFavorite)
        }
    }
}