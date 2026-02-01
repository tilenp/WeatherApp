package com.example.data.api

internal interface OpenWeatherMapServerConfig {
    val baseUrl: String
}

internal object OpenWeatherMapServer {
    val prod = object : OpenWeatherMapServerConfig {
        override val baseUrl: String = PROD_HOST
    }
}

private const val PROD_HOST = "https://api.openweathermap.org/"