package com.example.matule.domain.models.responses

data class ProfileInfoResult(
    val error: String?,
    val result: ProfileResult?
)

data class ProfileResult(
    val id: Long,
    val first_name: String,
    val last_name: String?,
    val avatar: String?,
    val address: String?,
    val balance: Float,
    val email: String
)
