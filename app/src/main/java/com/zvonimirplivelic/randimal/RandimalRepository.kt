package com.zvonimirplivelic.randimal

import com.zvonimirplivelic.randimal.remote.RetrofitInstance

class RandimalRepository {

    suspend fun getRandomAnimalInformation() = RetrofitInstance.api.getRandomAnimalInfo()
}