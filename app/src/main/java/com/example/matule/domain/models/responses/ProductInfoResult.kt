package com.example.matule.domain.models.responses

data class ProductInfoResult(
    val error: String?,
    val result: ProductInfoResultData?
)

data class ProductInfoResultData(
    val id: Long,
    val name: String,
    val description: String,
    val price: Float,
    val category: String,
    val brand: String,
    val image: List<String>,
    val is_liked: Boolean,
    val in_cart: Boolean
)