package com.example.flightsearchapp.di

import android.content.Context
import androidx.room.Room
import com.example.flightsearchapp.data.FlightSearchDao
import com.example.flightsearchapp.data.FlightSearchDatabase
import com.example.flightsearchapp.data.FlightSearchRepository
import com.example.flightsearchapp.data.FlightSearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): FlightSearchDatabase {
        return Room.databaseBuilder(
            appContext,
            FlightSearchDatabase::class.java,
            "bus_schedule_db"
        ).createFromAsset("database/flight_search.db") // Initializes from the assets file
            .fallbackToDestructiveMigration() // Optional, handles migrations
            .build()
    }

    @Provides
    fun provideFlightSearchDao(db: FlightSearchDatabase): FlightSearchDao {
        return db.flightSearchDao()
    }

    @Provides
    fun provideBusScheduleRepository(
        busScheduleDao: FlightSearchDao
    ): FlightSearchRepository {
        return FlightSearchRepositoryImpl(busScheduleDao)
    }
}