package com.example.flightsearchapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapp.screens.FlightViewmodel
import com.example.flightsearchapp.screens.HomeScreen


@Composable
fun FlightSearchApp(
    modifier: Modifier = Modifier,
    flightViewModel: FlightViewmodel = viewModel()
) {
    HomeScreen(
        viewmodel = flightViewModel,
        modifier = modifier
    )
}