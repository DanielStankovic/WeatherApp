package com.example.weatherapp.util

import com.example.weatherapp.R
import com.example.weatherapp.util.Constants.Companion.API_KEY

fun getImageByWeatherID(weatherID:String):Int{
    return with(weatherID){
        when{
           startsWith("2") -> R.drawable.thunder
           startsWith("3") -> R.drawable.drizzle
           startsWith("5") -> R.drawable.rain
           startsWith("6") -> R.drawable.snow
           startsWith("7") -> R.drawable.fog
           startsWith("800") -> R.drawable.clear
           startsWith("8") -> R.drawable.cloud
            else -> R.drawable.clear
        }
    }

}

fun getQueryParams(latitude:Double, longitude:Double) : HashMap<String, String>{
    val queries = HashMap<String, String>()
    queries["lat"] = latitude.toString()
    queries["lon"] = longitude.toString()
    queries["appid"] = API_KEY
    queries["units"] = "metric"
    return queries
}