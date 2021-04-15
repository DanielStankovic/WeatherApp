package com.example.weatherapp.ui.home

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*


@Entity
@Parcelize
data class Location(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val lat:Double,
    val lon:Double,
    val name:String,
    val dateCreated: Date
) : Parcelable
