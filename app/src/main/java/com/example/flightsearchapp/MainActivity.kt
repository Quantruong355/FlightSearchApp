package com.example.flightsearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flightsearchapp.screens.HomeScreen
import com.example.flightsearchapp.ui.theme.FlightSearchAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlightSearchAppTheme {
                FlightSearchApp()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlightSearchAppTheme {
        FlightSearchApp()
    }
}