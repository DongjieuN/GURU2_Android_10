package com.example.guru2_android_10

import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil


class MainVM:ViewModel() {


    val showNotificationEvent by lazy { SingleLiveEvent<Void>() }

    fun checkForGeoFenceEntry(userLocation: Location, geofenceLat: Double, geofenceLong: Double, radius: Double) {
        val startLatLng = LatLng(userLocation.latitude, userLocation.longitude)
        val geofenceLatLng = LatLng(geofenceLat, geofenceLong)

        val distanceInMeters = SphericalUtil.computeDistanceBetween(startLatLng, geofenceLatLng)

        if (distanceInMeters < radius) {
            // 사용자가 지오펜스 안으로 들어감을 알림
            showNotificationEvent.call()
        }
    }
}