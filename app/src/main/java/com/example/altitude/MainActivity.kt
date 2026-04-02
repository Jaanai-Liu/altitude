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

        // Initialize the GPS tracker
        val locationTracker = LocationTracker(this)

        enableEdgeToEdge()
        setContent {
            AltitudeTheme {
                // 1. Create state variables for the UI
                // These start with default searching text, and UI will automatically update when they change
                var altitudeText by remember { mutableStateOf("WAIT...") }
                var speedText by remember { mutableStateOf("SPEED -- km/h") }
                var latText by remember { mutableStateOf("LAT --") }
                var lonText by remember { mutableStateOf("LON --") }

                // 2. Prepare the permission request dialog
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    // Check if either fine or coarse location was granted
                    val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

                    if (isGranted) {
                        // 3. Fetch data if permission granted
                        locationTracker.getCurrentLocation { location ->
                            if (location != null) {
                                // Convert raw GPS data to UI strings
                                altitudeText = "${location.altitude.toInt()} m"
                                speedText = "SPEED ${(location.speed * 3.6).toInt()} km/h" // m/s to km/h
                                latText = "LAT ${location.latitude}"
                                lonText = "LON ${location.longitude}"
                            } else {
                                altitudeText = "NO GPS SIGNAL"
                            }
                        }
                    } else {
                        altitudeText = "PERMISSION DENIED"
                    }
                }

                // 4. Trigger the permission request when the app launches
                LaunchedEffect(Unit) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 5. Connect the live state variables to the UI component
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