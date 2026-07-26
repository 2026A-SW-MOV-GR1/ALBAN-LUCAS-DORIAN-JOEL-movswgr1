package com.equipoamazon

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform