package com.example.matule.domain.models.responses

data class BuyProductResponse(
    val error: String?,
    val result: BuyProductResponseResult?
)

data class BuyProductResponseResult(
    val user_id: Long,
    val product_id: Long,
    val count: Int,
    val status: String,
    val time: String
)