package com.example.altitude.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class LocationTracker(context: Context) {

    // Initialize the official Google Play Services location client
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    /**
     * Fetches the most recent and accurate location from the GPS sensor.
     * * @param onLocationReceived Callback triggered when location is retrieved successfully or fails.
     */
    @SuppressLint("MissingPermission") // Permissions will be handled in the UI layer before calling this
    fun getCurrentLocation(onLocationReceived: (Location?) -> Unit) {
        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location: Location? ->
            // Successfully retrieved location (contains altitude, latitude, longitude, speed)
            onLocationReceived(location)
        }.addOnFailureListener {
            // Failed to retrieve location
            onLocationReceived(null)
        }
    }
}