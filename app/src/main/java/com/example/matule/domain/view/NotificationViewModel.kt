package com.example.matule.domain.view

import androidx.lifecycle.ViewModel
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.RetrofitInstance
import com.example.matule.domain.models.responses.NotificationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationViewModel : ViewModel() {
    private val _notification = MutableStateFlow<NotificationResult?>(null)
    val notification: StateFlow<NotificationResult?> = _notification.asStateFlow()

    suspend fun getAllNotifications(
        preferencesManager: PreferencesManager,
    ) {
        try {
            val token = preferencesManager.getAuthData()
            if (token != null) {
                _notification.value = RetrofitInstance.apiService.getAllNotifications("Bearer ${token.accessToken}")
            }
        } catch (ex: Exception) {
            _notification.value = NotificationResult(ex.message, null)
        }
    }
}