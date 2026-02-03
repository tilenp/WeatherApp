package com.example.data.di

import com.example.data.repository.GeocodingRepositoryImpl
import com.example.data.repository.LocationRepositoryImpl
import com.example.data.repository.WeatherRepositoryImpl
import com.example.domain.repository.GeocodingRepository
import com.example.domain.repository.LocationRepository
import com.example.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindGeocodingRepository(geocodingRepositoryImpl: GeocodingRepositoryImpl): GeocodingRepository

    @Binds
    abstract fun bindLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository

    @Binds
    abstract fun bindWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository
}