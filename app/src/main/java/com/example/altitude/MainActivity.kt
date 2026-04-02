package com.example.altitude

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.altitude.location.LocationTracker
import com.example.altitude.ui.AltitudeScreen
import com.example.altitude.ui.theme.AltitudeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationTracker = LocationTracker(this)

        enableEdgeToEdge()
        setContent {
            AltitudeTheme {
                // Use short strings to prevent UI layout breaking
                var altitudeText by remember { mutableStateOf("-- m") }
                var speedText by remember { mutableStateOf("SPEED -- km/h") }
                var latText by remember { mutableStateOf("LAT --") }
                var lonText by remember { mutableStateOf("LON --") }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

                    if (isGranted) {
                        locationTracker.startTracking { location ->
                            altitudeText = "${location.altitude.toInt()} m"
                            speedText = "SPEED ${(location.speed * 3.6).toInt()} km/h"
                            latText = "LAT ${String.format("%.4f", location.latitude)}°"
                            lonText = "LON ${String.format("%.4f", location.longitude)}°"
                        }
                    } else {
                        altitudeText = "DENIED"
                    }
                }

                LaunchedEffect(Unit) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AltitudeScreen(
                        modifier = Modifier.padding(innerPadding),
                        altitude = altitudeText,
                        speed = speedText,
                        lat = latText,
                        lon = lonText
                    )
                }
            }
        }
    }
}