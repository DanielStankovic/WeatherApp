package com.example.weatherapp.data

import com.example.weatherapp.models.WeatherResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val weatherApi: WeatherApi
) {

    suspend fun getWeatherForLocation(queryMap : Map<String, String>) : Response<WeatherResponse>{
        val aa = weatherApi
        return  weatherApi.getWeatherForLocation(queryMap)
    }
}