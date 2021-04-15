package com.example.weatherapp.data

import androidx.lifecycle.LiveData
import com.example.weatherapp.ui.home.Location
import com.example.weatherapp.ui.LocationDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val locationDao: LocationDao
) {

    fun getLocations() : LiveData<List<Location>>{
        return locationDao.getLocations()
    }
    suspend fun insertLocation(location: Location) : Long {
      return  locationDao.insertLocation(location)
    }

    suspend fun deleteLocation(location: Location) : Int {
        return  locationDao.deleteLocation(location)
    }

    suspend fun checkIfAlreadyAdded(name:String) : Int?{
        return locationDao.checkIfAlreadyAdded(name)
    }
}