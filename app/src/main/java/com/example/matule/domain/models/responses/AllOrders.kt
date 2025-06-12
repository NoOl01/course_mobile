package com.example.matule.domain.models.responses

data class AllOrders(
    val error: String?,
    val result: List<AllOrdersResult>?
)

data class AllOrdersResult(
    val id: Long,
    val user_id: Long,
    val product_id: Long,
    val count: Int,
    val status: String,
    val time: String
)