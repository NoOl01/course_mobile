package com.example.matule.domain.view

import androidx.lifecycle.ViewModel
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.RetrofitInstance
import com.example.matule.domain.models.responses.ErrorResult
import com.example.matule.domain.models.responses.ProductsResultForCart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel : ViewModel() {
    private val _products = MutableStateFlow<ProductsResultForCart?>(null)
    val products: StateFlow<ProductsResultForCart?> = _products.asStateFlow()

    suspend fun getAllProducts(preferencesManager: PreferencesManager) {
        try {
            val token = preferencesManager.getAuthData()
            if (token != null) {
                _products.value =
                    RetrofitInstance.apiService.getAllCart("Bearer ${token.accessToken}")
            }
        } catch (ex: Exception) {
            _products.value = ProductsResultForCart(ex.message, emptyList())
        }
    }

    suspend fun addToCart(
        preferencesManager: PreferencesManager,
        productId: Long
    ): ErrorResult {
        return try {
            val token = preferencesManager.getAuthData()
            if (token != null) {
                RetrofitInstance.apiService.addToCart("Bearer ${token.accessToken}", productId)
            } else {
                ErrorResult("invalid token")
            }
        } catch (ex: Exception) {
            ErrorResult(ex.message!!)
        }
    }

    suspend fun deleteFromCart(
        preferencesManager: PreferencesManager,
        productId: Long
    ): ErrorResult {
        return try {
            val token = preferencesManager.getAuthData()
            if (token != null) {
                RetrofitInstance.apiService.deleteFromCart("Bearer ${token.accessToken}", productId)
            } else {
                ErrorResult("invalid token")
            }
        } catch (ex: Exception) {
            ErrorResult(ex.message!!)
        }
    }

    suspend fun updateInCartCount(
        preferencesManager: PreferencesManager,
        productId: Long,
        count: Int,
        action: String
    ): ErrorResult{
        return try {
            val token = preferencesManager.getAuthData()
            if (token != null) {
                RetrofitInstance.apiService.cartUpdateCount("Bearer ${token.accessToken}", productId, count, action)
            } else {
                ErrorResult("invalid token")
            }
        } catch (ex: Exception) {
            ErrorResult(ex.message!!)
        }
    }
}