package com.equipoamazon.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MapView(
    modifier: Modifier,
    destLat: Double,
    destLng: Double,
    hubLat: Double?,
    hubLng: Double?,
    currentLat: Double?,
    currentLng: Double?
)
