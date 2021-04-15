package com.example.weatherapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.ui.home.Location
import com.example.weatherapp.ui.LocationDao
import com.example.weatherapp.util.DateConverter

@Database(
    entities = [Location::class],
    version = 1,
    exportSchema = false

)
@TypeConverters(DateConverter::class)
abstract class WeatherDatabase : RoomDatabase(){
    abstract fun locationDao() : LocationDao
}