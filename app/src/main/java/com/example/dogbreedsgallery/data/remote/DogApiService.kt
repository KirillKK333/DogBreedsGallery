package com.example.dogbreedsgallery.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DogApiService {

    @GET("breeds")
    suspend fun getBreeds(
        @Header("x-api-key") apiKey: String = "live_nFWz7a1AGgjahEAXUU08GomuKghCgf3VdbA7ky1zrnU9i78134nbOdeISUsmFlBI"
    ): List<BreedDto>

    @GET("images/search")
    suspend fun getImagesByBreed(
        @Query("breed_ids") breedIds: String,
        @Query("limit") limit: Int = 20,
        @Header("x-api-key") apiKey: String = "live_nFWz7a1AGgjahEAXUU08GomuKghCgf3VdbA7ky1zrnU9i78134nbOdeISUsmFlBI"
    ): List<ImageDto>
}