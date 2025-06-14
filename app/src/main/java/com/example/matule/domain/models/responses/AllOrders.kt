package com.example.matule.domain.models.responses

data class AllOrders(
    val error: String?,
    val result: List<AllOrdersResult>?
)

data class AllOrdersResult(
    val id: Long,
    val product_id: Long,
    val product: OrderResultProduct,
    val count: Int,
    val price: Float,
    val status: String,
    val time: String
)

data class OrderResultProduct(
    val id: Long,
    val name: String,
    val description: String,
    val size: Int,
    val season: String,
    val price: Float,
    val category_id: Long,
    val brand_id: Long,
    val images: List<OrderResultProductImage>
)

data class OrderResultProductImage(
    val file_path: String
)