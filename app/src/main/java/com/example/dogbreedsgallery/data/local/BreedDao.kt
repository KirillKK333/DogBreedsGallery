package com.example.dogbreedsgallery.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {

    @Query("SELECT * FROM breeds ORDER BY name ASC")
    fun getAllBreeds(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteBreeds(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE id = :id")
    suspend fun getBreedById(id: Int): BreedEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreeds(breeds: List<BreedEntity>)

    @Query("UPDATE breeds SET isFavorite = :isFavorite WHERE id = :breedId")
    suspend fun setFavorite(breedId: Int, isFavorite: Boolean)

    @Query("DELETE FROM breeds")
    suspend fun clearAll()
}