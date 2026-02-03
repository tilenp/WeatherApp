package com.example.weatherapp.navigation

internal sealed class Destination(val route: String) {
    object Forecast : Destination("Forecast")
    object CitySearch : Destination("CitySearch")
}