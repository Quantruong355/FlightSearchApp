package com.example.flightsearchapp.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InputPreferenceRepository @Inject constructor(
    @ApplicationContext val context: Context
){

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    val textInput : Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[TEXT_INPUT_KEY] ?: ""
    }

    suspend fun saveTextInput(textInput: String) {
        context.dataStore.edit { preferences ->
            preferences[TEXT_INPUT_KEY] = textInput
        }
    }

    companion object {
        val USER_PREFERENCES_NAME = "user_preferences"
        val TEXT_INPUT_KEY = stringPreferencesKey("text_input")
    }
}