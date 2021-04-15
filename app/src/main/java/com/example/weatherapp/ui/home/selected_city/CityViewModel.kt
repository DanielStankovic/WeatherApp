package com.example.weatherapp.ui.home.selected_city

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.util.NetworkResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class CityViewModel @ViewModelInject constructor(
    private val repository: WeatherRepository,
    application: Application
) : AndroidViewModel(application) {
    private val cityEventChannel = Channel<NetworkResult<WeatherResponse>>()
    val cityEvent = cityEventChannel.receiveAsFlow()

    fun getWeatherForLocation(queryMap : Map<String, String>) = viewModelScope.launch {
        cityEventChannel.send(NetworkResult.Loading())
        try {
            val response = repository.remote.getWeatherForLocation(queryMap)
            cityEventChannel.send(handleResponseFromServer(response))
        }catch (e : Exception){
               cityEventChannel.send(NetworkResult.Error("Error from server: ${e.message}"))
        }
    }

    private fun handleResponseFromServer(response: Response<WeatherResponse>): NetworkResult<WeatherResponse> {

        when{
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.body() == null -> {
                return NetworkResult.Error("Data for the location not found")
            }
            !response.isSuccessful -> {
                return NetworkResult.Error("Error from server: ${response.errorBody()}")
            }
            response.isSuccessful -> {
               val weatherData = response.body()
                return NetworkResult.Success(weatherData!!)
            }else -> {
                return NetworkResult.Error(response.message())
            }
        }

    }
}