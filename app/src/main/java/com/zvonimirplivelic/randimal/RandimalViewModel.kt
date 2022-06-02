package com.zvonimirplivelic.randimal

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zvonimirplivelic.randimal.model.AnimalDataResponse
import com.zvonimirplivelic.randimal.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class RandimalViewModel(
    val app: Application
) : AndroidViewModel(app) {
    private val randimalRepository: RandimalRepository

    val randomAnimalInfo: MutableLiveData<Resource<AnimalDataResponse>> = MutableLiveData()
    var randomAnimalResponse: AnimalDataResponse? = null

    init {
        randimalRepository = RandimalRepository()
    }

    fun getAnimalInformation() = viewModelScope.launch {
        safeRandomAnimalInformationCall()
    }

    private suspend fun safeRandomAnimalInformationCall() {
        randomAnimalInfo.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = randimalRepository.getRandomAnimalInformation()
                delay(444L)
                randomAnimalInfo.postValue(handleRandomFactResponse(response))
            } else {
                randomAnimalInfo.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> randomAnimalInfo.postValue(Resource.Error("Network Failure"))
                else -> randomAnimalInfo.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleRandomFactResponse(response: Response<AnimalDataResponse>): Resource<AnimalDataResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                randomAnimalResponse = resultResponse

                return Resource.Success(randomAnimalResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<RandimalApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
        return false
    }
}