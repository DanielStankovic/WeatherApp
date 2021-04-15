package com.example.weatherapp.ui

import android.app.Application
import android.location.Geocoder
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.ui.home.Location
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*

class LocationViewModel @ViewModelInject constructor(
    private val repository: WeatherRepository,
    application: Application
) : AndroidViewModel(application){

    private val locationEventChannel = Channel<LocationViewModelEvent>()
    val locationEvent = locationEventChannel.receiveAsFlow()
    val locationList = repository.local.getLocations()

    fun insertLocation(markerOptions: MarkerOptions) = viewModelScope.launch {
        try {
            if(markerOptions.title == null){
                locationEventChannel.send(LocationViewModelEvent.LocationEvent("Location has no title..."))
                return@launch
            }
            val alreadyAddedID = repository.local.checkIfAlreadyAdded(markerOptions.title)
            if(alreadyAddedID != null){
                locationEventChannel.send(LocationViewModelEvent.LocationEvent("Current location already added..."))
                return@launch
            }
            val insertedLocID = repository.local.insertLocation(
                markerOptions.let {
                    Location(lat = it.position.latitude,
                        lon = it.position.longitude, name = it.title, dateCreated = Date()
                    )
                }
            )
            if(insertedLocID > 0){
                locationEventChannel.send(LocationViewModelEvent.LocationEvent("Location bookmarked successfully"))
            }else{
                locationEventChannel.send(LocationViewModelEvent.LocationEvent("Error bookmarking the location. Try again!"))
            }
        } catch (e: Exception) {
            locationEventChannel.send(LocationViewModelEvent.LocationEvent("Error bookmarking: ${e.message}"))

        }
    }

    fun deleteLocation(location: Location) = viewModelScope.launch {
        try {
            val numOfDeleted = repository.local.deleteLocation(location)
            if(numOfDeleted > 0){
                locationEventChannel.send(LocationViewModelEvent.LocationEvent("Location deleted successfully"))
            }else{
                locationEventChannel.send(LocationViewModelEvent.LocationEvent("Error deleting the location. Try again!"))
            }
        } catch (e: Exception) {
            locationEventChannel.send(LocationViewModelEvent.LocationEvent("Error deleting: ${e.message}"))
        }
    }

    fun getGeoData(geocoder: Geocoder, it: LatLng) = viewModelScope.launch {

        try {
            locationEventChannel.send(LocationViewModelEvent.LoadingEvent)
            val adddresses = it.let { it1 -> geocoder.getFromLocation(it1.latitude, it.longitude, 1) }
            if(adddresses == null){
                locationEventChannel.send(LocationViewModelEvent.LocationEvent("Error getting location"))
                return@launch
            }
            val cityName = if (adddresses.isEmpty()) "Error getting city name..." else adddresses[0]?.locality
           val marker = MarkerOptions().position(it).title(cityName)
            locationEventChannel.send(LocationViewModelEvent.MarkerReadyEvent(marker))
        } catch (e: Exception) {
            locationEventChannel.send(LocationViewModelEvent.LocationEvent("Error getting location: ${e.message}"))

        }

    }

    sealed class LocationViewModelEvent{
        object LoadingEvent: LocationViewModelEvent()
        data class LocationEvent(val message:String) : LocationViewModelEvent()
        data class MarkerReadyEvent(val markerOptions: MarkerOptions) : LocationViewModelEvent()
    }

}