package com.example.matule.domain.models.requests

data class CheckOtp(
    val email: String,
    val code: Int
)
