package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.data.WeatherDatabase
import com.example.weatherapp.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun getDatabaseObject(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, WeatherDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun getLocationDao(database: WeatherDatabase) = database.locationDao()
}