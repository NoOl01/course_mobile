package com.example.matule.domain.view

import androidx.lifecycle.ViewModel
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.RetrofitInstance
import com.example.matule.domain.models.responses.ErrorResult
import com.example.matule.domain.models.responses.ProductsResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavouriteViewModel : ViewModel() {
    private val _products = MutableStateFlow<ProductsResult?>(null)
    val products: StateFlow<ProductsResult?> = _products.asStateFlow()

    suspend fun getAllProducts(preferencesManager: PreferencesManager) {
        try {
            val token = preferencesManager.getAuthData()
            if (token != null) {
                _products.value =
                    RetrofitInstance.apiService.getAllFavourite("Bearer ${token.accessToken}")
            }
        } catch (ex: Exception) {
            _products.value = ProductsResult(ex.message, emptyList())
        }
    }

    suspend fun addToFavourite(
        preferencesManager: PreferencesManager,
        productId: Long
    ): ErrorResult {
        return try {
            val token = preferencesManager.getAuthData()
            if (token != null) {
                RetrofitInstance.apiService.addToFavourite("Bearer ${token.accessToken}", productId)
            } else {
                ErrorResult("invalid token")
            }
        } catch (ex: Exception) {
            ErrorResult(ex.message!!)
        }
    }

    suspend fun deleteFromFavourite(
        preferencesManager: PreferencesManager,
        productId: Long
    ): ErrorResult {
        return try {
            val token = preferencesManager.getAuthData()
            if (token != null) {
                RetrofitInstance.apiService.deleteFromFavourite("Bearer ${token.accessToken}", productId)
            } else {
                ErrorResult("invalid token")
            }
        } catch (ex: Exception) {
            ErrorResult(ex.message!!)
        }
    }
}