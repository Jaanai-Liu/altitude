package com.example.altitude.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

class LocationTracker(context: Context) {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    fun startTracking(onLocationReceived: (Location) -> Unit) {
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Only raw hardware GPS data will trigger this now
                onLocationReceived(location)
            }

            override fun onProviderDisabled(provider: String) {}
            override fun onProviderEnabled(provider: String) {}
            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        // Strictly use the physical GPS hardware for maximum accuracy
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000L, // Update every 2 seconds
                1f,    // Update every 1 meter of movement
                locationListener
            )
        }
    }
}