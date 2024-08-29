package com.example.flightsearchapp.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearchapp.data.FlightSearchRepository
import com.example.flightsearchapp.data.InputPreferenceRepository
import com.example.flightsearchapp.model.Airport
import com.example.flightsearchapp.model.Favorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class FlightViewmodel @Inject constructor(
    private val repository: FlightSearchRepository,
    private val inputPreferenceRepository: InputPreferenceRepository
) : ViewModel() {

    private var _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _airports = MutableStateFlow<List<Airport>>(emptyList())
    val airports: StateFlow<List<Airport>> = _airports.asStateFlow()

    private val _possibleDestinations = MutableStateFlow<List<Airport>>(emptyList())
    val possibleDestinations: StateFlow<List<Airport>> = _possibleDestinations.asStateFlow()

    private val _favorites = MutableStateFlow<List<Favorite>>(emptyList())
    val favorites: StateFlow<List<Favorite>> = _favorites.asStateFlow()

    private var _selectedAirport = MutableStateFlow<Airport?>(null)
    val selectedAirport: StateFlow<Airport?> = _selectedAirport.asStateFlow()


    //Edit text in dataStore
    val textInput: StateFlow<String> = inputPreferenceRepository.textInput
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5),
            initialValue = ""
        )

    fun onAirportSelected(airport: Airport) {
        _selectedAirport.value = airport
    }

    fun onSearchTextChanged(newText: String) {
        _searchText.value = newText
        viewModelScope.launch {
            inputPreferenceRepository.saveTextInput(newText)
            repository.getAirportByCodeAndName(newText).collect {
                _airports.value = it
            }
        }
        getPossibleDestinations()
    }

//    fun getFavorites() {
//        viewModelScope.launch {
//            repository.getAllFavorite().collect {
//                _favorites.value = it
//            }
//        }
//    }

    val favoriteRoutes : StateFlow<List<Favorite>> = repository.getAllFavorite()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5),
            initialValue = emptyList()
        )
    
    fun insertFavorite(departure: String, destination: String) {
        viewModelScope.launch {
            val favorite = Favorite(
                0,
                departure,
                destination
            )
            repository.insertFavorite(favorite)
        }
    }

    fun getPossibleDestinations() {
        viewModelScope.launch {
            repository.getAllAirport().collect { airports ->
                val filteredAirports = airports.filter { it.iataCode != selectedAirport.value?.iataCode }
                _possibleDestinations.value = filteredAirports
            }
        }
    }
}




