package com.example.matule.domain.view

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.RetrofitInstance
import com.example.matule.domain.models.requests.SettingsMoneyActionRequest
import com.example.matule.domain.models.responses.ErrorResult

class SettingsViewModel : ViewModel() {
    suspend fun moneyAction(
        preferencesManager: PreferencesManager,
        action: String,
        money: Float
    ): ErrorResult {
        return try {
            Log.d("SETTINGS", "START")
            val token = preferencesManager.getAuthData()
            if (token != null) {
                Log.d("SETTINGS", "tokens: $token")
                val req = SettingsMoneyActionRequest(action, money)
                Log.d("SETTINGS", "req: $req")
                val resp = RetrofitInstance.apiService.moneyAction("Bearer ${token.accessToken}", req)
                Log.d("SETTINGS", "resp: $resp")
                resp
            } else {
                ErrorResult("Invalid credentials")
            }
        } catch (ex: Exception){
            ErrorResult(ex.message)
        }
    }
}