package com.example.matule.domain.models.responses

data class DaDataAddressResponse(
    val suggestions: List<DaDataAddressResponseSuggestion>
)

data class DaDataAddressResponseSuggestion(
    val value: String
)
