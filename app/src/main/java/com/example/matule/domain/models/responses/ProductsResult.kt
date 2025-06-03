package com.example.matule.domain.models.responses

data class ProductsResult(
    val error: String?,
    val result: List<ProductsResultData>
)

data class ProductsResultData(
    val id: Long,
    val name: String,
    val price: Float,
    val category_id: Long,
    val brand_id: Long,
    val image: String,
    val is_liked: Boolean,
    val in_cart: Boolean
)