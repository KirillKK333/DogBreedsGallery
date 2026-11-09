package com.example.dogbreedsgallery.data

import com.example.dogbreedsgallery.data.local.BreedDao
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.data.remote.DogApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class BreedRepository(
    private val dao: BreedDao,
    private val api: DogApiService
) {

    val allBreeds: Flow<List<BreedEntity>> = dao.getAllBreeds()
    val favoriteBreeds: Flow<List<BreedEntity>> = dao.getFavoriteBreeds()

    suspend fun refreshBreeds(): Result<Unit> {
        return try {
            val remote = api.getBreeds()
            // Получаем текущий список из БД (Flow.first() возвращает List<BreedEntity>)
            val favoritesSnapshot: List<BreedEntity> = dao.getAllBreeds().first()

            val entities = remote.map { dto ->
                val wasFavorite = favoritesSnapshot
                    .firstOrNull { it.id == dto.id }?.isFavorite == true
                BreedEntity(
                    id = dto.id,
                    name = dto.name,
                    temperament = dto.temperament.orEmpty(),
                    origin = dto.origin,
                    lifeSpan = dto.lifeSpan.orEmpty(),
                    description = dto.bredFor,
                    imageUrl = dto.image?.url ?: "",
                    isFavorite = wasFavorite
                )
            }
            dao.clearAll()
            dao.insertBreeds(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleFavorite(breedId: Int, current: Boolean) {
        dao.setFavorite(breedId, !current)
    }

    suspend fun getImagesForBreed(breedId: Int): List<String> {
        return try {
            api.getImagesByBreed(breedIds = breedId.toString(), limit = 20)
                .map { it.url }
        } catch (e: Exception) {
            emptyList()
        }
    }
}