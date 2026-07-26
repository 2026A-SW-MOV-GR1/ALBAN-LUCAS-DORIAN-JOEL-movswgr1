package com.equipoamazon

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipoamazon.ui.*
import com.equipoamazon.viewmodel.UltimaMillaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(viewModel: UltimaMillaViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    AmazonTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(AmazonOrange, RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("U", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Última Milla", color = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = AmazonDark)
                )
            },
            bottomBar = {
                BottomPanel(
                    onCancel = { /* Lógica de cancelar */ },
                    onConfirm = { viewModel.confirmDelivery() },
                    enabled = uiState.paquete != null && uiState.currentLat != null
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(SurfaceLight)
            ) {
                // Barra de Progreso
                ProgressBarSection()

                // Mapa (Ocupa el espacio restante entre secciones)
                Box(modifier = Modifier.weight(1f)) {
                    val pkg = uiState.paquete
                    if (pkg != null) {
                        MapView(
                            modifier = Modifier.fillMaxSize(),
                            destLat = pkg.latOrigen,
                            destLng = pkg.lngOrigen,
                            hubLat = uiState.hubLat,
                            hubLng = uiState.hubLng,
                            currentLat = uiState.currentLat,
                            currentLng = uiState.currentLng
                        )
                        
                        // Panel de Validación de Datos Recibidos
                        ValidationOverlay(
                            id = pkg.idPaquete,
                            hubLat = uiState.hubLat,
                            hubLng = uiState.hubLng,
                            estado = pkg.estado
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = AmazonOrange)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Esperando conexión con App 2...", color = AmazonGrey)
                                Text("Usa la acción DISTRIBUCION_TO_ULTIMA_MILLA", fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                    }

                    // Card de Coordenadas GPS Actuales
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        CoordinateCard(uiState.currentLat, uiState.currentLng)
                    }
                }
            }
        }
    }
}

@Composable
fun ValidationOverlay(id: String, hubLat: Double?, hubLng: Double?, estado: String) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .widthIn(max = 300.dp),
        colors = CardDefaults.cardColors(containerColor = AmazonDark.copy(alpha = 0.9f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("✅ DATOS VINCULADOS", color = AmazonOrange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("ID: $id", color = Color.White, fontSize = 11.sp)
            Text("Estado: $estado", color = SuccessGreen, fontSize = 11.sp)
            Text(
                "Hub Origen: ${if (hubLat != null) "$hubLat, $hubLng" else "No recibido"}",
                color = Color.White,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun ProgressBarSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProgressSegment("Paso 1", "Admisión", SuccessGreen, isComplete = true)
        HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 4.dp), color = SuccessGreen, thickness = 2.dp)
        ProgressSegment("Paso 2", "Distribución", SuccessGreen, isComplete = true)
        HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 4.dp), color = AmazonOrange, thickness = 2.dp)
        ProgressSegment("Paso 3", "Última Milla", AmazonOrange, isComplete = false)
    }
}

@Composable
fun ProgressSegment(step: String, label: String, color: Color, isComplete: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(color, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (isComplete) {
                Text("✓", color = Color.White, fontSize = 12.sp)
            } else {
                Text("3", color = Color.White, fontSize = 12.sp)
            }
        }
        Text(label, fontSize = 10.sp, color = AmazonDark)
    }
}

@Composable
fun CoordinateCard(lat: Double?, lng: Double?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, AmazonOrange, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(AmazonOrange, RoundedCornerShape(6.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = if (lat != null && lng != null) "Lat: $lat, Lng: $lng" else "Obteniendo GPS...",
                    fontSize = 14.sp,
                    color = AmazonDark,
                    fontWeight = FontWeight.Medium
                )
                Text("Ubicación actual del transportista", fontSize = 11.sp, color = AmazonGrey)
            }
        }
    }
}

@Composable
fun BottomPanel(onCancel: () -> Unit, onConfirm: () -> Unit, enabled: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Cancelar", color = AmazonDark)
            }
            Button(
                onClick = onConfirm,
                enabled = enabled,
                modifier = Modifier.weight(2f),
                colors = ButtonDefaults.buttonColors(containerColor = AmazonOrange),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Confirmar Entrega", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
