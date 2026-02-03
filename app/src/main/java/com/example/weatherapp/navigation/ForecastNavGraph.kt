package com.example.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.screens.forecast.view.ForecastScreen
import com.example.weatherapp.screens.searchcity.view.SearchCityScreen

@Composable
fun ForecastNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Forecast.route,
    ) {
        composable(route = Destination.Forecast.route) {
            ForecastScreen(
                navigateToSearchCityDestination = { navController.navigate(Destination.CitySearch.route) },
            )
        }
        composable(route = Destination.CitySearch.route) {
            SearchCityScreen(
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
