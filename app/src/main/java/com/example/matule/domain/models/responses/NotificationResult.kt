package com.example.matule.domain.models.responses

data class NotificationResult(
    val error: String?,
    val result: List<NotificationResultData>?
)

data class NotificationResultData(
    val id: Long,
    val title: String,
    val description: String,
    val user_id: Long,
    val date_time: String
)