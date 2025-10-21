package com.example.newsapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapsViewModel : ViewModel() {
    var selectedLocation by mutableStateOf<LatLng?>(null)
        private set

    fun onMapTapped(latLng: LatLng) {
        selectedLocation = latLng
    }
}