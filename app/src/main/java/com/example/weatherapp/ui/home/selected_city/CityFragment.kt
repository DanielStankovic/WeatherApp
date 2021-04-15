package com.example.weatherapp.ui.home.selected_city

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentCityBinding
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.util.NetworkResult
import com.example.weatherapp.util.getImageByWeatherID
import com.example.weatherapp.util.getQueryParams
import com.example.weatherapp.util.viewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class CityFragment : Fragment(R.layout.fragment_city) {

    private val binding:FragmentCityBinding by viewBinding(FragmentCityBinding::bind)
    private val args by navArgs<CityFragmentArgs>()
    private val viewModel:CityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.cityEvent.collect { event ->
            when(event){
                is NetworkResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.nestedScrollView.visibility = View.VISIBLE
                    setupData(event.data)
                }
                is NetworkResult.Error -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.appBarLayout.visibility = View.GONE
                    binding.nestedScrollView.visibility = View.GONE
                    Snackbar.make(binding.cityCoordinatorLayout, event.message.toString(), Snackbar.LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.appBarLayout.visibility = View.GONE
                    binding.nestedScrollView.visibility = View.GONE
                }
            }

        }
    }
        viewModel.getWeatherForLocation(getQueryParams(args.selectedCity.lat, args.selectedCity.lon))
    }

    private fun setupData(data: WeatherResponse?) {
        binding.apply {
            data?.let {
                collapsingToolbar.title = args.selectedCity.name
                Glide.with(requireContext()).load(getImageByWeatherID(data.weather[0].id.toString())).into(cityWeatherIv)
                weatherMainTv.text = data.weather[0].main
                currentTempTv.text = "${data.main.temp} \u2103"
                feelsLikeTempTv.text = "Feels like: ${data.main.feelsLike} \u2103"
                minTempTv.text = "Min temp: ${data.main.tempMin} \u2103"
                maxTempTv.text = "Max temp: ${data.main.tempMax} \u2103"
                windTv.text = "Wind: ${data.wind.speed} km/h"
                visibilityTv.text = "Visibility: ${data.visibility/1000} km"

            }
        }

    }
}