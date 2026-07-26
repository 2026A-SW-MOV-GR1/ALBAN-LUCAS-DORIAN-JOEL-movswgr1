package com.equipoamazon.models

import kotlinx.serialization.Serializable

@Serializable
data class Paquete(
    val idPaquete: String,
    val remitente: String,
    val destinatario: String,
    val direccionOrigen: String,
    val latOrigen: Double,
    val lngOrigen: Double,
    val fechaAdmision: String,
    val estado: String
)
