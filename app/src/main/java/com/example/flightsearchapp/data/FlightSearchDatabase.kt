package com.example.flightsearchapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.flightsearchapp.model.Airport
import com.example.flightsearchapp.model.Favorite

@Database(entities = [Airport::class,Favorite::class], version = 1)
abstract class FlightSearchDatabase : RoomDatabase() {

    abstract fun flightSearchDao(): FlightSearchDao
}