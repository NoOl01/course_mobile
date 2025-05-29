package com.example.matule.domain.models.responses

data class CategoryResult(
    val error: String?,
    val result: List<Result?>
)

data class Result(
    val id: Long,
    val name: String
)