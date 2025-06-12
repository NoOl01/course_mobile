package com.example.matule.domain.models.requests

data class ResetPassword(
    val email: String,
    val token: String,
    val password: String
)
