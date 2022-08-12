package com.neu.madcourse.mad_team4_finalproject.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import java.io.IOException


class LocationUtils(context: Context) {

    /* The Activity context using the utilities */
    private val mContext: Context = context

    /* The Base utils reference */
    private val mBaseUtils: BaseUtils = BaseUtils(mContext)

    /* The Location request reference */
    private val mLocationRequest: LocationRequest =
        LocationRequest.create()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setInterval(5000)
            .setFastestInterval(2000)

    /* The Location manager reference */
    private val mLocationManager: LocationManager =
        mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    /* The Geocoder instance */
    private val mGeocoder: Geocoder = Geocoder(mContext)

    /* The Location permission request code */
    private val REQUEST_CODE_LOCATION = 101

    /* The Resolution permission request code */
    private val REQUEST_CODE_RESOLUTION = 102

    @SuppressLint("ObsoleteSdkInt")
    private fun getCurrentLocation(): Location? {
        var location: Location? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if permissions are granted
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Check GPS enabled status
                if (isGPSEnabled()) {
                    // Make the location request
                    LocationServices.getFusedLocationProviderClient(mContext)
                        .requestLocationUpdates(mLocationRequest, object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                super.onLocationResult(locationResult)

                                // Stop the location request
                                LocationServices.getFusedLocationProviderClient(mContext)
                                    .removeLocationUpdates(this)
                                if (locationResult.locations.size > 0) {
                                    location = locationResult.locations[0]
                                }
                            }
                        }, Looper.getMainLooper())
                } else {
                    enableGPS(mLocationRequest)
                }
            } else {
                ActivityCompat.requestPermissions(mContext as Activity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_LOCATION
                )
            }
        }

        return location
    }

    /**
     * Helper method to extract address information from user input
     *
     * @param inputQuery The location input query from the user
     * @return the state code if the location exists
     */
    fun getLocationFromQuery(inputQuery: String): Address? {
        // The Address reference
        var locationAddress: Address? = null
        try {
            // Extract the list of addresses from the location text
            val addressList = mGeocoder.getFromLocationName(inputQuery, 1)
            // Extract the first address
            if (addressList.isNotEmpty()) locationAddress = addressList[0]
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return locationAddress
    }

    /**
     * Helper method to extract address information from user's current location
     *
     * @param location The current location of the user
     * @return the state code if the location exists
     */
    fun getLocationFromCoordinates(location: Location?): Address? {
        // The Address reference
        var locationAddress: Address? = null
        if (location != null) {
            try {
                // Extract the list of addresses from the location text
                val addressList =
                    mGeocoder.getFromLocation(location.latitude, location.longitude, 1)
                // Extract the first address
                if (addressList.isNotEmpty()) locationAddress = addressList[0]
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return locationAddress
    }

    /* Helper method to enable the device GPS */
    fun enableGPS(locationRequest: LocationRequest) {
        // Build the location settings request
        val requestBuilder: LocationSettingsRequest.Builder =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        requestBuilder.setAlwaysShow(true)

        // Build the response task
        val responseTask: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(mContext)
                .checkLocationSettings(requestBuilder.build())

        // Execute the response task
        responseTask.addOnCompleteListener { task ->
            try {
                // Extract the location settings response
                val response: LocationSettingsResponse = task.getResult(ApiException::class.java)
                mBaseUtils.showToast("GPS is already enabled!", Toast.LENGTH_SHORT)
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    // Resolution code
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            // Resolve the GPS exception
                            val resolvableException = exception as ResolvableApiException
                            resolvableException.startResolutionForResult(
                                mContext as Activity,
                                REQUEST_CODE_RESOLUTION
                            )
                        } catch (exception: IntentSender.SendIntentException) {
                            exception.printStackTrace()
                        }
                    }

                    // Device does not have the GPS driver
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }

    /* Helper method to check whether the device GPS is enabled */
    fun isGPSEnabled(): Boolean {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}