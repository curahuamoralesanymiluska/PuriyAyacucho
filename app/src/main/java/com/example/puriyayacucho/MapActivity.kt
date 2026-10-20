package com.example.puriyayacucho

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var latitude: Double = -13.1519
    private var longitude: Double = -74.2145
    private var placeName: String = "Lugar Turístico"
    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        latitude = intent.getDoubleExtra("latitude", -13.1519)
        longitude = intent.getDoubleExtra("longitude", -74.2145)
        placeName = intent.getStringExtra("placeName") ?: "Lugar Turístico"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        val btnNavigate = findViewById<Button>(R.id.btnNavigate)
        btnNavigate.setOnClickListener {
            abrirEnGoogleMaps()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        try {
            val location = LatLng(latitude, longitude)
            val marker = mMap.addMarker(MarkerOptions()
                .position(location)
                .title(placeName)
                .snippet("Ubicación de $placeName"))

            if (marker != null) {
                marker.showInfoWindow()
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

            // Solicitar permisos de ubicación si es necesario
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                mMap.isMyLocationEnabled = true
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Error al cargar el mapa: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun abrirEnGoogleMaps() {
        try {
            GuiaActivity.abrirConDestino(this, placeName ?: "Destino", latitude, longitude)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al abrir la Guía", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                        mMap.isMyLocationEnabled = true
                    }
                }
            }
        }
    }
}
