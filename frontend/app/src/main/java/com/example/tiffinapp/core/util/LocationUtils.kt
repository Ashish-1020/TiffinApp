package com.example.tiffinapp.core.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

object LocationUtils {

    private const val PREFS_NAME = "user_location_prefs"
    private const val KEY_LAT = "latitude"
    private const val KEY_LNG = "longitude"
    private const val KEY_ADDR = "address"

    /**
     * Fetch current location and store it in SharedPreferences
     */
    fun fetchAndStoreLocation(context: Context, onResult: (Boolean) -> Unit) {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onResult(false)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lng = location.longitude

                // Convert coordinates to address
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                val address = if (!addresses.isNullOrEmpty()) {
                    addresses[0].getAddressLine(0)
                } else {
                    "Address not found"
                }

                // Store in SharedPreferences
                val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                prefs.edit().apply {
                    putString(KEY_LAT, lat.toString())
                    putString(KEY_LNG, lng.toString())
                    putString(KEY_ADDR, address)
                    apply()
                }

                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    /**
     * Get stored latitude
     */
    fun getLatitude(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LAT, null)
    }

    /**
     * Get stored longitude
     */
    fun getLongitude(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LNG, null)
    }

    /**
     * Get stored address
     */
    fun getAddress(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_ADDR, null)
    }
}
