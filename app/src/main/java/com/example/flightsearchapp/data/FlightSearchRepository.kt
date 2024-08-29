package com.example.flightsearchapp.data

import com.example.flightsearchapp.model.Airport
import com.example.flightsearchapp.model.Favorite
import kotlinx.coroutines.flow.Flow

interface FlightSearchRepository {

    fun getAirportByCodeAndName(input: String): Flow<List<Airport>>

    fun getAllAirport(): Flow<List<Airport>>

    fun getAllFavorite(): Flow<List<Favorite>>

    suspend fun insertFavorite(favorite: Favorite)
}