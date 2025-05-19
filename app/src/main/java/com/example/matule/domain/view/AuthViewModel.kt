package com.example.matule.domain.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.RetrofitInstance
import com.example.matule.domain.models.requests.Login
import com.example.matule.domain.models.requests.Registration
import com.example.matule.domain.models.responses.AuthModelResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewMode : ViewModel() {
    private val _auth = MutableStateFlow<AuthModelResult?>(null)
    val auth: StateFlow<AuthModelResult?> = _auth.asStateFlow()

    fun registration(
        name: String,
        email: String,
        password: String,
        preferencesManager: PreferencesManager
    ) {
        viewModelScope.launch {
            try {
                val newReg = Registration(name, email, password)
                val response = RetrofitInstance.apiService.register(newReg)
                if (response.result != null && response.error == null){
                    preferencesManager.saveAuthData(
                        response.result.access_token,
                        response.result.refresh_token
                    )
                    _auth.value = response
                } else {
                    _auth.value = AuthModelResult(null, "Invalid credentials")
                }
            } catch (ex: Exception) {
                _auth.value = AuthModelResult(null, ex.message)
            }
        }
    }

    fun login(
        email: String,
        password: String,
        preferencesManager: PreferencesManager
    ) {
        viewModelScope.launch {
            try {
                val newReg = Login(email, password)
                val response = RetrofitInstance.apiService.login(newReg)
                if (response.result != null && response.error == null){
                    preferencesManager.saveAuthData(
                        response.result.access_token,
                        response.result.refresh_token
                    )
                    _auth.value = response
                } else {
                    _auth.value = AuthModelResult(null, "Invalid credentials")
                }
            } catch (ex: Exception) {
                _auth.value = AuthModelResult(null, ex.message)
            }
        }
    }
}