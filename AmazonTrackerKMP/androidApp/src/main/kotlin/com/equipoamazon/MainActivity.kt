package com.equipoamazon

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.equipoamazon.models.Paquete
import com.equipoamazon.viewmodel.UltimaMillaViewModel
import com.google.android.gms.location.*
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {

    private val viewModel: UltimaMillaViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        handleIntent(intent)
        requestLocationPermissions()

        setContent {
            App(viewModel = viewModel)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val jsonExtra = intent?.getStringExtra("paquete_json")
        val hubLat = intent?.getDoubleExtra("hub_lat", 0.0) ?: 0.0
        val hubLng = intent?.getDoubleExtra("hub_lng", 0.0) ?: 0.0

        if (jsonExtra != null) {
            try {
                val paquete = Json.decodeFromString<Paquete>(jsonExtra)
                viewModel.setPaquete(paquete)
                if (hubLat != 0.0 && hubLng != 0.0) {
                    viewModel.setHubLocation(hubLat, hubLng)
                }
                Log.d("UltimaMilla", "Paquete recibido: ${paquete.idPaquete} desde Hub: $hubLat, $hubLng")
            } catch (e: Exception) {
                Log.e("UltimaMilla", "Error al deserializar paquete", e)
            }
        }
    }

    private fun requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1001
            )
            return
        }
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setMinUpdateIntervalMillis(2000)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        locationResult.lastLocation?.let { location ->
                            viewModel.updateLocation(location.latitude, location.longitude)
                        }
                    }
                },
                Looper.getMainLooper()
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        }
    }
}
