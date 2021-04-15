package com.example.weatherapp.ui

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.ui.home.Location

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location) : Long

    @Delete
    suspend fun deleteLocation(location: Location): Int

    @Query("SELECT * FROM Location ORDER BY dateCreated DESC")
    fun getLocations() : LiveData<List<Location>>

    @Query("SELECT id FROM Location WHERE name=:name")
    suspend fun checkIfAlreadyAdded(name:String) : Int?
}