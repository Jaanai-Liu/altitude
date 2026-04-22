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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.altitude.location.LocationTracker
import com.example.altitude.sensor.CompassTracker
import com.example.altitude.ui.AltitudeScreen
import com.example.altitude.ui.theme.AltitudeTheme

class MainActivity : ComponentActivity() {

    private var currentAzimuth = mutableFloatStateOf(0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationTracker = LocationTracker(this)
        val compassTracker = CompassTracker(this)

        // Start compass immediately on launch (Zero delay)
        compassTracker.startTracking { azimuth ->
            currentAzimuth.floatValue = azimuth
        }

        enableEdgeToEdge()
        setContent {
            AltitudeTheme {
                // REQUIREMENT: Keep altitude text short ("-- m") to avoid breaking the large UI font.
                // Move the long "searching" status text to the bottom latitude field instead.
                var altitudeText by remember { mutableStateOf("-- m") }
                var speedText by remember { mutableStateOf("SPEED -- km/h") }
                var latText by remember { mutableStateOf("SEARCHING SATELLITES...") }
                var lonText by remember { mutableStateOf("") }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

                    if (isGranted) {
                        locationTracker.startTracking { location ->
                            // Update altitude only if we have a valid 3D fix (> 0.0)
                            if (location.altitude == 0.0) {
                                altitudeText = "-- m"
                            } else {
                                altitudeText = "${location.altitude.toInt()} m"
                            }

                            // Always update speed and coordinates when we have a fix
                            speedText = "SPEED ${(location.speed * 3.6).toInt()} km/h"
                            latText = "LAT ${String.format("%.4f", location.latitude)}°"
                            lonText = "LON ${String.format("%.4f", location.longitude)}°"
                        }
                    } else {
                        // If denied, keep altitude short and show error at the bottom
                        altitudeText = "-- m"
                        latText = "PERMISSION DENIED"
                        lonText = ""
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
                        // modifier = Modifier.padding(innerPadding),
                        modifier = Modifier,
                        altitude = altitudeText,
                        speed = speedText,
                        lat = latText,
                        lon = lonText,
                        azimuth = currentAzimuth.floatValue
                    )
                }
            }
        }
    }
}