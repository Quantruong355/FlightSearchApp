package com.example.flightsearchapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapp.FlightSearchApp
import com.example.flightsearchapp.R
import com.example.flightsearchapp.model.Airport
import com.example.flightsearchapp.model.Favorite
import com.example.flightsearchapp.ui.theme.FlightSearchAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewmodel: FlightViewmodel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name)
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = colorResource(id = R.color.background_color)
                ),
                modifier = modifier
            )
        }
    ) { innerPadding ->
        HomeContent(
            viewmodel = viewmodel,
            modifier = Modifier.padding(15.dp),
            contentPaddingValues = innerPadding
        )
    }
}

@Composable
fun HomeContent(
    viewmodel: FlightViewmodel,
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp)
) {
    var isFlightContentVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(contentPaddingValues),
    ) {
        val list by viewmodel.airports.collectAsState()
        val searchText by viewmodel.searchText.collectAsState()

        SearchBar(
            viewModel = viewmodel,
            onChange = { isFlightContentVisible = false }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ) {
            if (searchText.isBlank() && searchText.isNullOrEmpty()) {
                FlightContent(viewmodel = viewmodel)
            } else {
                if (isFlightContentVisible) {
                    FlightContent(viewmodel = viewmodel)
                } else {
                    SuggestList(
                        list = list,
                        onClickitem = {
                            viewmodel.onAirportSelected(it)
                            isFlightContentVisible = true
                        })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    viewModel: FlightViewmodel,
    onChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    val searchText by viewModel.searchText.collectAsState()
    val text by viewModel.textInput.collectAsState()

    TextField(
        value = text,
        onValueChange = { newText ->
            viewModel.onSearchTextChanged(newText)
            onChange()
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null
            )
        },
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            containerColor = colorResource(id = R.color.search_color),
        ),
        placeholder = { Text(stringResource(id = R.string.search_placeholder)) },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(55.dp)
    )
}

@Composable
fun SuggestList(
    list: List<Airport>,
    onClickitem: (Airport) -> Unit,
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.padding(contentPaddingValues)
    ) {
        items(list) { airport ->
            SuggestItem(
                airport = airport,
                onClickitem = onClickitem,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SuggestItem(
    airport: Airport,
    onClickitem: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .clickable {
                onClickitem(airport)
            },
    ) {
        Text(
            text = airport.iataCode,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = airport.name,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun FlightContent(
    viewmodel: FlightViewmodel,
    modifier: Modifier = Modifier
) {
    val searchText by viewmodel.searchText.collectAsState()
    val fav_list by viewmodel.favoriteRoutes.collectAsState()
    val filter_list by viewmodel.possibleDestinations.collectAsState()
    val airport by viewmodel.selectedAirport.collectAsState()

    Column(modifier = modifier) {
        Text(
            text = stringResource(
                id = if (searchText.isNotBlank() && searchText.isNotEmpty()) {
                    R.string.flight_departure
                } else {
                    R.string.flight_fav
                },
                searchText.uppercase()
            ),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 20.dp),
        ) {
            if (searchText.isNotBlank() && searchText.isNotEmpty()) {
                items(filter_list) { item ->
                    FlightItem(
                        viewmodel = viewmodel,
                        airportDes = airport!!,
                        airportArrive = item
                    )
                }
            } else {
                items(fav_list) { fav ->
                    FavoriteItem(favorite = fav)
                }
            }
        }
    }
}

@Composable
fun FlightItem(
    viewmodel: FlightViewmodel,
    airportDes: Airport,
    airportArrive: Airport,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = colorResource(id = R.color.item_color),
                shape = RoundedCornerShape(
                    topEnd = 20.dp
                )
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(15.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.weight(1f)

            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.depart),
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row {
                        Text(
                            text = airportDes.iataCode,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = airportDes.name,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Column {
                    Text(
                        text = stringResource(id = R.string.arrive),
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row {
                        Text(
                            text = airportArrive.iataCode,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = airportArrive.name,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            IconButton(
                onClick = { viewmodel.insertFavorite(airportDes.iataCode, airportArrive.iataCode)},
                modifier = Modifier.padding(end = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}

@Composable
fun FavoriteItem(
    favorite: Favorite,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = colorResource(id = R.color.item_color),
                shape = RoundedCornerShape(
                    topEnd = 20.dp
                )
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(15.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.weight(1f)

            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.depart),
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row {
                        Text(
                            text = favorite.departureCode,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "",
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Column {
                    Text(
                        text = stringResource(id = R.string.arrive),
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row {
                        Text(
                            text = favorite.destinationCode,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "",
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            IconButton(
                onClick = {},
                modifier = Modifier.padding(end = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.Blue.copy(alpha = 0.5f),
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun SearchBarPreview() {
    FlightSearchAppTheme {
//        SearchBar()
    }
}

@Preview
@Composable
private fun SuggestListPreview() {
    FlightSearchAppTheme {
        val list = listOf(
            Airport(
                id = 1,
                iataCode = "AAA",
                name = "Airport AAA",
                passengers = 100
            ),
            Airport(
                id = 2,
                iataCode = "BBB",
                name = "Airport BBB",
                passengers = 200
            )
        )
        Surface {
            SuggestList(
                onClickitem = {},
                list = list
            )
        }
    }
}

@Preview
@Composable
private fun FlightItemPreview() {
    FlightSearchAppTheme {
        val list = listOf(
            Airport(
                id = 1,
                iataCode = "AAA",
                name = "Airport AAA",
                passengers = 100
            ),
            Airport(
                id = 2,
                iataCode = "BBB",
                name = "Airport BBB",
                passengers = 200
            )
        )
        Surface {
//            FlightContent(airports = list)
        }
    }
}

