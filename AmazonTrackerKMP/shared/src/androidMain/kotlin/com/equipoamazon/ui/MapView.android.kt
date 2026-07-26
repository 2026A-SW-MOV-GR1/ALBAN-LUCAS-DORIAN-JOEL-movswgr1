package com.equipoamazon.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun MapView(
    modifier: Modifier,
    destLat: Double,
    destLng: Double,
    hubLat: Double?,
    hubLng: Double?,
    currentLat: Double?,
    currentLng: Double?
) {
    val destination = LatLng(destLat, destLng)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(destination, 13f)
    }

    // Centrar cámara para abarcar puntos importantes
    LaunchedEffect(destLat, destLng, hubLat, hubLng) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(destination, 13f)
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // 1. Marcador de Destino Final (Amazon Orange)
        Marker(
            state = MarkerState(position = destination),
            title = "Destino Final",
            snippet = "Entrega de Paquete",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
        )

        // 2. Marcador del Hub de Origen (Enviado por App 2)
        hubLat?.let { hLat ->
            hubLng?.let { hLng ->
                val hubPos = LatLng(hLat, hLng)
                Marker(
                    state = MarkerState(position = hubPos),
                    title = "Punto de Origen (Hub)",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                )

                // Línea de ruta: Hub -> Destino
                Polyline(
                    points = listOf(hubPos, destination),
                    color = AmazonOrange,
                    width = 8f
                )
            }
        }

        // 3. Marcador de Mi Ubicación Actual (GPS Transportista)
        currentLat?.let { cLat ->
            currentLng?.let { cLng ->
                val currentPos = LatLng(cLat, cLng)
                Marker(
                    state = MarkerState(position = currentPos),
                    title = "Tu Ubicación Actual",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
                )
            }
        }
    }
}
