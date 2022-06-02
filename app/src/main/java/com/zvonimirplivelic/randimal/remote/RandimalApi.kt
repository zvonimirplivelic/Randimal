package com.zvonimirplivelic.randimal.remote

import com.zvonimirplivelic.randimal.model.AnimalDataResponse
import retrofit2.Response
import retrofit2.http.GET

interface RandimalApi {
    @GET("animals/rand")
    suspend fun getRandomAnimalInfo(): Response<AnimalDataResponse>
}