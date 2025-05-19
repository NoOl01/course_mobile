package com.example.matule.domain.models.responses

data class AuthModelResult(
    val result: AuthModelTokens?,
    val error: String?
)

data class AuthModelTokens(
    val access_token: String,
    val refresh_token: String
)