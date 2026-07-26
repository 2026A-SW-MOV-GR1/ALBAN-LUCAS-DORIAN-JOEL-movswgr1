package com.equipoamazon.viewmodel

import androidx.lifecycle.ViewModel
import com.equipoamazon.models.Paquete
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UltimaMillaState(
    val paquete: Paquete? = null,
    val hubLat: Double? = null,
    val hubLng: Double? = null,
    val currentLat: Double? = null,
    val currentLng: Double? = null,
    val isDelivered: Boolean = false,
    val isLoading: Boolean = false
)

class UltimaMillaViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UltimaMillaState())
    val uiState: StateFlow<UltimaMillaState> = _uiState.asStateFlow()

    fun setPaquete(paquete: Paquete) {
        _uiState.value = _uiState.value.copy(paquete = paquete)
    }

    fun setHubLocation(lat: Double, lng: Double) {
        _uiState.value = _uiState.value.copy(hubLat = lat, hubLng = lng)
    }

    fun updateLocation(lat: Double, lng: Double) {
        _uiState.value = _uiState.value.copy(currentLat = lat, currentLng = lng)
    }

    fun confirmDelivery() {
        _uiState.value = _uiState.value.copy(isDelivered = true)
        // Aquí iría la lógica para notificar al servidor o App 2
    }
}
