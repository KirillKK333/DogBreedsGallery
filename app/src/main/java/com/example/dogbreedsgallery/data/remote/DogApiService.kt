package com.example.dogbreedsgallery.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DogApiService {

    @GET("breeds")
    suspend fun getBreeds(
        @Header("x-api-key") apiKey: String = "live_your_api_key_if_you_have_one"
    ): List<BreedDto>

    @GET("images/search")
    suspend fun getImagesByBreed(
        @Query("breed_ids") breedIds: String,
        @Query("limit") limit: Int = 20,
        @Header("x-api-key") apiKey: String = "live_your_api_key_if_you_have_one"
    ): List<ImageDto>
}