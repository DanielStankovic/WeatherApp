package com.example.weatherapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.ui.LocationViewModel
import com.example.weatherapp.util.viewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment(R.layout.fragment_home), LocationAdapter.OnItemClickListener {


    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: LocationViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = LocationAdapter(this)
        binding.locationRv.setHasFixedSize(true)
        binding.locationRv.adapter = adapter
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                v: RecyclerView,
                h: RecyclerView.ViewHolder,
                t: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(h: RecyclerView.ViewHolder, dir: Int) =
                adapter.removeAt(h.adapterPosition)
        }).attachToRecyclerView(binding.locationRv)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.locationEvent.collect { event ->
               when(event){
                   is LocationViewModel.LocationViewModelEvent.LocationEvent ->  Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                   is LocationViewModel.LocationViewModelEvent.MarkerReadyEvent -> {

                   }
                   LocationViewModel.LocationViewModelEvent.LoadingEvent -> {

                   }
               }
            }
        }

        viewModel.locationList.observe(viewLifecycleOwner) {
            it?.let {
                toggleRecyclerViewVisibility(it.isEmpty())
                adapter.submitList(it)
            }
        }

    }

    private fun toggleRecyclerViewVisibility(isListEmpty: Boolean) {
        binding.apply {
            if (isListEmpty) locationRv.visibility = View.GONE else locationRv.visibility =
                View.VISIBLE
            if (isListEmpty) locationNoBookmarkedTv.visibility =
                View.VISIBLE else locationNoBookmarkedTv.visibility = View.GONE
        }
    }

    override fun onItemClicked(location: Location) {

        val action = HomeFragmentDirections.actionHomeFragmentToCityFragment(location)
        findNavController().navigate(action)

    }

    override fun onItemDeleted(location: Location) {
        viewModel.deleteLocation(location)
    }
}