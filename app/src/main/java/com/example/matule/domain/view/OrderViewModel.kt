package com.example.matule.domain.view

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.RetrofitInstance
import com.example.matule.domain.models.requests.BuyProductRequest
import com.example.matule.domain.models.responses.AllOrders
import com.example.matule.domain.models.responses.BuyProductResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderViewModel : ViewModel() {
    private val _order = MutableStateFlow<BuyProductResponse?>(null)
    val order: StateFlow<BuyProductResponse?> = _order.asStateFlow()

    private val _allOrders = MutableStateFlow<AllOrders?>(null)
    val allOrders: StateFlow<AllOrders?> = _allOrders.asStateFlow()

    suspend fun buyProduct(preferencesManager: PreferencesManager, productId: Long, count: Int) {
        try {
            val token = preferencesManager.getAuthData()
            if (token != null) {
                val req = BuyProductRequest(productId, count)
                val resp = RetrofitInstance.apiService.buyProduct(
                    "Bearer ${token.accessToken}",
                    req
                )
                Log.d("ORDER", resp.toString())
                _order.value = resp
            }
        } catch (ex: Exception) {
            _order.value = BuyProductResponse(ex.message, null)
        }
    }

    suspend fun getAllOrders(preferencesManager: PreferencesManager) {
        try {
            val token = preferencesManager.getAuthData()
            if (token != null) {
                _allOrders.value = RetrofitInstance.apiService.getAllOrders("Bearer ${token.accessToken}")
            }
        } catch (ex: Exception) {
            _allOrders.value = AllOrders(ex.message, emptyList())
        }
    }
}