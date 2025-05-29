package com.example.matule.domain.models.requests

data class UpdateProfile (
    val first_name: String,
    val last_name: String,
    val address: String,
    val email: String
)