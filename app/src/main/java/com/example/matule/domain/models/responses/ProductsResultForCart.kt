package com.example.matule.domain.models.responses

data class ProductsResultForCart(
    val error: String?,
    val result: List<ProductsResultForCartData>
)

data class ProductsResultForCartData(
    val id: Long,
    val name: String,
    val price: Float,
    val image: String,
    val is_liked: Boolean,
    val in_cart: Boolean,
    val count: Int
)