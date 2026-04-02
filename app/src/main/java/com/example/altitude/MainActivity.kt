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
import com.example.altitude.sensor.CompassTracker
import com.example.altitude.ui.AltitudeScreen
import com.example.altitude.ui.theme.AltitudeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationTracker = LocationTracker(this)
        val compassTracker = CompassTracker(this) // Initialize Compass

        enableEdgeToEdge()
        setContent {
            AltitudeTheme {
                var altitudeText by remember { mutableStateOf("-- m") }
                var speedText by remember { mutableStateOf("SPEED -- km/h") }
                var latText by remember { mutableStateOf("LAT --") }
                var lonText by remember { mutableStateOf("LON --") }
                var currentAzimuth by remember { mutableStateOf(0f) } // Compass State

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

                // Trigger location permissions and start compass on launch
                LaunchedEffect(Unit) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )

                    // Start listening to rotation sensor
                    compassTracker.startTracking { azimuth ->
                        currentAzimuth = azimuth
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AltitudeScreen(
                        modifier = Modifier.padding(innerPadding),
                        altitude = altitudeText,
                        speed = speedText,
                        lat = latText,
                        lon = lonText,
                        azimuth = currentAzimuth // Pass the live azimuth to the UI
                    )
                }
            }
        }
    }
}