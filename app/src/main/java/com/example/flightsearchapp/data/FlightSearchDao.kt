package com.example.flightsearchapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.flightsearchapp.model.Airport
import com.example.flightsearchapp.model.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightSearchDao {

    @Query(
        "SELECT * FROM airport WHERE iata_code LIKE '%' || :input || '%' " +
                "OR name LIKE '%' || :input || '%' ORDER BY passengers DESC"
    )
    fun getAirportByCodeAndName(input: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport")
    fun getAllAirport(): Flow<List<Airport>>

    @Query("SELECT * FROM favorite")
    fun getAllFavorite(): Flow<List<Favorite>>

    @Insert
    suspend fun insertFavorite(favorite: Favorite)
}