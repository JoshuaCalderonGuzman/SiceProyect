package com.example.siceproyect.data

data class LoginResult(
    val success: Boolean,
    val mensaje: String = "",
    val cookies: List<String> = emptyList()
)