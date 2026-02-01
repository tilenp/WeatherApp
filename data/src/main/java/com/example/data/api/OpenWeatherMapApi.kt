package com.example.data.api

import com.example.data.model.GeocodingItemDto
import com.example.data.model.WeatherDataDto
import com.example.domain.model.Units
import retrofit2.http.GET
import retrofit2.http.Query

internal interface OpenWeatherMapApi {

    @GET("data/3.0/onecall")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("units") units: Units,
        @Query("lang") lang: String,
    ): WeatherDataDto

    @GET("geo/1.0/direct")
    suspend fun getDirectGeocoding(
        @Query("q") q: String,
        @Query("limit") limit: Int,
    ): List<GeocodingItemDto>

    @GET("geo/1.0/reverse")
    suspend fun getReverseGeocoding(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("limit") limit: Int,
    ): List<GeocodingItemDto>
}