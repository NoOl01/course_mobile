package com.example.matule.domain.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.RetrofitInstance
import com.example.matule.domain.models.requests.CheckOtp
import com.example.matule.domain.models.requests.Login
import com.example.matule.domain.models.requests.Registration
import com.example.matule.domain.models.requests.ResetPassword
import com.example.matule.domain.models.requests.SendEmail
import com.example.matule.domain.models.responses.AuthModelResult
import com.example.matule.domain.models.responses.CheckOtpResponse
import com.example.matule.domain.models.responses.ErrorResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    suspend fun registration(
        name: String,
        email: String,
        password: String,
        preferencesManager: PreferencesManager
    ): AuthModelResult {

        return try {
            val request = Registration(name, email, password)
            val response = RetrofitInstance.apiService.register(request)
            if (response.result != null && response.error == null) {
                preferencesManager.saveAuthData(
                    response.result.access_token,
                    response.result.refresh_token
                )
                response
            } else {
                AuthModelResult(null, "Invalid credentials")
            }
        } catch (ex: Exception) {
            AuthModelResult(null, ex.message)
        }

    }

    suspend fun login(
        email: String,
        password: String,
        preferencesManager: PreferencesManager
    ): AuthModelResult {
        return try {
            val request = Login(email, password)
            val response = RetrofitInstance.apiService.login(request)
            if (response.result != null && response.error == null) {
                preferencesManager.saveAuthData(
                    response.result.access_token,
                    response.result.refresh_token
                )
                response
            } else {
                AuthModelResult(null, "Invalid credentials")
            }
        } catch (ex: Exception) {
            AuthModelResult(null, ex.message)
        }
    }

    suspend fun sendEmail(email: String): ErrorResult? {
        return try {
            val req = SendEmail(email)
            RetrofitInstance.apiService.sendEmail(req)
        } catch (ex: Exception) {
            ErrorResult(ex.message!!)
        }
    }

    suspend fun checkOtp(email: String, code: Int): CheckOtpResponse {
        return try {
            val req = CheckOtp(email = email, code = code)
            RetrofitInstance.apiService.checkOtp(req)
        } catch (ex: Exception) {
            CheckOtpResponse(ex.message, null)
        }
    }

    suspend fun resetPassword(email: String, token: String, password: String): ErrorResult {
        return try {
            val req = ResetPassword(email, token, password)
            RetrofitInstance.apiService.resetPassword(req)
        } catch (ex: Exception){
            ErrorResult(ex.message)
        }
    }

    private suspend fun refresh(refreshToken: String): AuthModelResult {
        return try {
            RetrofitInstance.apiService.refresh(refreshToken)
        } catch (ex: Exception) {
            AuthModelResult(null, ex.message)
        }
    }

    fun refreshToken(preferencesManager: PreferencesManager) {
        viewModelScope.launch {
            val token = preferencesManager.getAuthData()
            if (token != null) {
                val result = refresh("Refresh ${token.refreshToken}")
                result.result?.let {
                    preferencesManager.saveAuthData(
                        it.access_token,
                        it.refresh_token
                    )
                }
            }
        }
    }

    fun startPeriodicTokenRefresh(preferencesManager: PreferencesManager) {
        viewModelScope.launch {
            while (true) {
                delay(55 * 60 * 1000L)
                refreshToken(preferencesManager)
            }
        }
    }

}