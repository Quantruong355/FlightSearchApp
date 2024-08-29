package com.example.flightsearchapp.data

import com.example.flightsearchapp.model.Airport
import com.example.flightsearchapp.model.Favorite
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FlightSearchRepositoryImpl @Inject constructor(
    private val flightDao : FlightSearchDao
) : FlightSearchRepository {
    override fun getAirportByCodeAndName(input: String): Flow<List<Airport>> =
        flightDao.getAirportByCodeAndName(input)

    override fun getAllAirport(): Flow<List<Airport>> =
        flightDao.getAllAirport()


    override fun getAllFavorite(): Flow<List<Favorite>> =
        flightDao.getAllFavorite()

    override suspend fun insertFavorite(favorite: Favorite) =
        flightDao.insertFavorite(favorite)
}