package com.example.newsapp

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.newsapp.ui.theme.NewsAppTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Locale

class MapsActivity : ComponentActivity() {
    private val viewModel: MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                MapsScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(viewModel: MapsViewModel) {
    val context = LocalContext.current
    val selectedLocation = viewModel.selectedLocation

    val initialLocation = LatLng(51.5074, -0.1278) // London
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 5f)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Select a Location for News") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    viewModel.onMapTapped(latLng)
                }
            ) {
                if (selectedLocation != null) {
                    Marker(
                        state = MarkerState(position = selectedLocation),
                        title = "Selected Location"
                    )
                }
            }

            Button(
                onClick = {
                    selectedLocation?.let { location ->
                        // Use Geocoder to find the country name
                        try {
                            val geocoder = Geocoder(context, Locale.getDefault())
                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            if (addresses != null && addresses.isNotEmpty()) {
                                val countryName = addresses[0].countryName
                                if (countryName != null) {
                                    val intent = Intent(context, ResultsActivity::class.java).apply {
                                        putExtra("SEARCH_TERM", countryName)
                                    }
                                    context.startActivity(intent)
                                } else {
                                    Toast.makeText(context, "Could not determine country.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Could not find address.", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Geocoder service not available.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = selectedLocation != null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Search News For This Location")
            }
        }
    }
}