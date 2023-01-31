package com.example.guru2_android_10


import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.guru2_android_10.R.mipmap.ic_launcher
import com.example.guru2_android_10.R.mipmap.ic_main
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity.NOTIFICATION_SERVICE as NOTIFICATION_SERVICE1


@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val LOCATION_PERMISSION = 101
        private const val LOCATION_REQUEST_CODE = 102

        private var GEOFENCE_LAT = 37.605980
        private var GEOFENCE_LONG = 127.047070

        private const val GEOFENCE_RADIUS = 700.00
        private const val CHANNEL_ID = "200"
        private const val NOTIFICATION_ID = 103
        private const val CHANNEL_NAME = "PushNotificationChannel"
    }

    private lateinit var viewModel:MainVM
    private var map: GoogleMap? = null
    private val locationRequest = LocationRequest()
    private var initiateMapZoom = true
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //위치 권한 허가를 받고 지도 만들기
        askLocationPermission()


        //회원정보 수정 버튼. 누르면 회원 정보 수정 화면으로 넘어감
        var modify_button = findViewById<Button>(R.id.modify)

        modify_button.setOnClickListener {
            val intent = Intent(this, activity_modify::class.java)
            startActivity(intent)
        }

        var sos_button = findViewById<Button>(R.id.sos)

        //sos 버튼을 구르면 119에 문자가 가도록 하는 기능
        sos_button.setOnClickListener {
            var intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("smsto:119")
            intent.putExtra("sms_body", "치매 환자 응급 상황입니다. 현재 위치: 서울시 노원구 \n 현재 복용중인 약: \n 지병: \n")
            startActivity(intent)
        }


    }

    private fun prepareActivity()
    {
        viewModel = ViewModelProvider(this).get(MainVM::class.java)
        initObservers()
        initMap()
        initFusedLocationClient()
        initUIComponent()
    }

   //위치 허가 부분
    private fun isLocationPermissionGranted():Boolean{
        return  ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun askLocationPermission()
    {
        if (!isLocationPermissionGranted())
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, arrayOf(permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION)
            }
            else
            {
                ActivityCompat.requestPermissions(this, arrayOf(permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION)
            }
        }
        else
        {
            prepareActivity()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode)
        {
            LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (isLocationPermissionGranted())
                    {
                        prepareActivity()
                    }
                }
                else
                {
                    askLocationPermission()
                }
                return
            }
        }
    }


    private fun initUIComponent(){
        val btnUpdateGeoFenceZone = findViewById<MaterialButton>(R.id.btnUpdateGeoFenceZone)

        val edtGeofenceZone = findViewById<AutoCompleteTextView>(R.id.edtGeofenceZone)
        val edtGeofenceRadius = findViewById<EditText>(R.id.edtGeofenceRadius)


        btnUpdateGeoFenceZone.setOnClickListener {
            if (!edtGeofenceZone.text.isNullOrEmpty() && !edtGeofenceRadius.text.isNullOrEmpty())
            {
                addCircleToMap(edtGeofenceZone.text.toString(),edtGeofenceRadius.text.toString().toDouble())
            }
        }
    }

    //현재 위치에 마커 추가
    private fun initFusedLocationClient(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                map?.addMarker(MarkerOptions().position(LatLng(location.latitude, location.longitude)).title("Current Location"))
            }
        }
    }

    private fun addCircleToMap(input:String,radius:Double){
        val g = Geocoder(this)

        var addressList: List<Address> = listOf()

        try
        {
            addressList = g.getFromLocationName(input, 1) as List<Address>
        }
        catch (e: IOException)
        {
            Toast.makeText(this, "위치를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            Log.d("","error catched at addCircleToMap: $e")
        }
        finally
        {
            //0 인덱스에 요소가 없는 경우에도 addressList가 비어 있지 않은지 확인
            if (addressList.isNotEmpty())
            {
                val address = addressList[0]
                if (address.hasLatitude() && address.hasLongitude()) {
                    map?.clear()
                    map?.addCircle(getGeofenceZone(address.latitude,address.longitude,radius))
                }
            }
            else
            {
                Toast.makeText(this, "위치를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //지오펜스 만들기
    private fun getGeofenceZone(lat:Double,lon:Double,radius:Double):CircleOptions{
        return CircleOptions()
            .center(LatLng(lat, lon))
            .radius(radius)
            .strokeColor(ContextCompat.getColor(this, R.color.borderGeofenceZone))
            .fillColor(ContextCompat.getColor(this, R.color.inGeofenceZone))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode)
        {
            LOCATION_REQUEST_CODE -> when (resultCode)
            {
                RESULT_OK ->
                {
                    requestMyGpsLocation { location ->
                        if (initiateMapZoom)
                        {
                            initiateMapZoom = false
                            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 10F))
                        }
                    }
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.showNotificationEvent.observe(this) { showNotification() }
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun showNotification() {

        val title = "원하는 위치 입성"
        val notificationManager =
        applicationContext.getSystemService(androidx.appcompat.app.AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        Toast.makeText(this, "원하는 위치에 있습니다", Toast.LENGTH_SHORT).show()

        val channel: NotificationChannel?



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(
                com.example.guru2_android_10.MainActivity.Companion.CHANNEL_ID,
                com.example.guru2_android_10.MainActivity.Companion.CHANNEL_NAME, IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setAutoCancel(true)
            .setSmallIcon(ic_main)
        NotificationCompat.PRIORITY_HIGH.also { builder.priority = it }
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        setupMap(googleMap)
        getCurrentLocation()
    }

    //구글 맵 사용
    private fun setupMap(googleMap: GoogleMap?) {
        map = googleMap
        map?.uiSettings?.isScrollGesturesEnabled = true
        map?.uiSettings?.setAllGesturesEnabled(true)
        map?.uiSettings?.isMyLocationButtonEnabled = true
        map?.addCircle(getGeofenceZone(GEOFENCE_LAT, GEOFENCE_LONG, GEOFENCE_RADIUS))
    }

    private fun getCurrentLocation() {
        map?.isMyLocationEnabled = true
        locationRequest.interval = 5000
        locationRequest.smallestDisplacement = 10f
        locationRequest.fastestInterval = 3000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)

        val task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        task.addOnCompleteListener {
            try
            {
                it.getResult(ApiException::class.java)
                requestMyGpsLocation { location ->
                    if (initiateMapZoom)
                    {
                        initiateMapZoom = false
                        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 10F))
                    }
                }
            }
            catch (exception: ApiException)
            {
                when (exception.statusCode)
                {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        try
                        {
                            val resolvable = exception as ResolvableApiException
                            resolvable.startResolutionForResult(this, LOCATION_REQUEST_CODE)
                        }
                        catch (e: IntentSender.SendIntentException)
                        {
                            Log.d("", "exception catched at getCurrentLocation: $e")
                        }
                        catch (e: ClassCastException)
                        {
                            Log.d("", "exception catched at getCurrentLocation: $e")
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> { }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestMyGpsLocation(callback: (location: Location) -> Unit) {
        val client = LocationServices.getFusedLocationProviderClient(this)
        client.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                val location = locationResult?.lastLocation
                if (location != null)
                {
                    callback.invoke(location)
                    viewModel.checkForGeoFenceEntry(location, GEOFENCE_LAT, GEOFENCE_LONG, GEOFENCE_RADIUS)
                }
            }
        }, null)
    }

}


