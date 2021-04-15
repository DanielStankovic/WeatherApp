package com.example.weatherapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ItemLocationBinding


class LocationAdapter (private val listener:OnItemClickListener) :
    ListAdapter<Location, LocationAdapter.LocationViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationAdapter.LocationViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }


    override fun onBindViewHolder(holder: LocationAdapter.LocationViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class LocationViewHolder(private val binding: ItemLocationBinding):
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                locationItemCv.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClicked(getItem(position))
                    }
                }

            }
        }

        fun bind(location: Location) {
            binding.apply {
                locationNameTv.text = location.name
                dateAddedTv.text = "Added: ${location.dateCreated}"
            }
        }
    }
    fun removeAt(position: Int){
        listener.onItemDeleted(getItem(position))
    }
    interface OnItemClickListener{
        fun onItemClicked(location: Location)
        fun onItemDeleted(location: Location)
    }

    class DiffCallback : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Location, newItem: Location) =
            oldItem == newItem
    }
}