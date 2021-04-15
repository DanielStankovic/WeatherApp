package com.example.weatherapp.ui.map

import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentMapBinding
import com.example.weatherapp.ui.LocationViewModel
import com.example.weatherapp.util.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import java.util.*

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val viewModel: LocationViewModel by activityViewModels()
    private lateinit var currentMarker:MarkerOptions
    private lateinit var googleMapReference: GoogleMap
   private lateinit var geocoder :Geocoder
    private val callback = OnMapReadyCallback { googleMap ->
        googleMapReference = googleMap
        googleMap.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(44.787197, 20.457273), 7.0f))
            setOnMapClickListener {
                try {
                    clear()
                   viewModel.getGeoData(geocoder, it)
                } catch (e: Exception) {
                    Snackbar.make(binding.mapCoordinatorLayout, "Error: ${e.message}", Snackbar.LENGTH_LONG).show()

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
       geocoder = Geocoder(requireContext(), Locale.getDefault())

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.locationEvent.collect {
                event ->
                when(event){
                    is LocationViewModel.LocationViewModelEvent.LocationEvent ->  {
                        binding.mapProgressBar.visibility = View.GONE
                        Snackbar.make(binding.mapCoordinatorLayout, event.message, Snackbar.LENGTH_LONG).show()
                    }
                    is LocationViewModel.LocationViewModelEvent.MarkerReadyEvent -> {
                        binding.mapProgressBar.visibility = View.GONE
                        googleMapReference.addMarker(event.markerOptions).showInfoWindow()
                        currentMarker = event.markerOptions
                    }
                    LocationViewModel.LocationViewModelEvent.LoadingEvent -> {
                        binding.mapProgressBar.visibility = View.VISIBLE
                    }
                }
            }

        }

        binding.addLocationFab.setOnClickListener(){
            if(this::currentMarker.isInitialized)
            viewModel.insertLocation(currentMarker)
            else
                Snackbar.make(binding.mapCoordinatorLayout, "No location selected...", Snackbar.LENGTH_LONG).show()
        }

        mapFragment?.getMapAsync(callback)
    }
}