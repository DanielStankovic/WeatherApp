package com.example.weatherapp.data

import com.example.weatherapp.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WeatherApi {

    @GET("/data/2.5/weather")
   suspend fun getWeatherForLocation(
        @QueryMap queryMap: Map<String, String>
    ) : Response<WeatherResponse>

}