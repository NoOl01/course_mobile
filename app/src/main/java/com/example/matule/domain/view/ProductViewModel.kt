package com.example.matule.domain.view

import androidx.lifecycle.ViewModel
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.RetrofitInstance
import com.example.matule.domain.models.responses.ProductInfoResult
import com.example.matule.domain.models.responses.ProductsResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductViewModel : ViewModel() {
    private val _products = MutableStateFlow<ProductsResult?>(null)
    val products: StateFlow<ProductsResult?> = _products.asStateFlow()

    private val _productInfo = MutableStateFlow<ProductInfoResult?>(null)
    val productInfo: StateFlow<ProductInfoResult?> = _productInfo.asStateFlow()

    suspend fun getAllProducts(preferencesManager: PreferencesManager) {
        try {
            val token = preferencesManager.getAuthData()
            if (token != null) {
                _products.value =
                    RetrofitInstance.apiService.getAllProducts("Bearer ${token.accessToken}")
            }
        } catch (ex: Exception) {
            _products.value = ProductsResult(ex.message, emptyList())
        }
    }

    suspend fun getProductInfo(preferencesManager: PreferencesManager, productId: Long) {
        try {
            val token = preferencesManager.getAuthData()
            if (token != null) {
                _productInfo.value = RetrofitInstance.apiService.getProductInfo(
                    "Bearer ${token.accessToken}",
                    productId
                )
            }
        } catch (ex: Exception) {
            _productInfo.value = ProductInfoResult(ex.message, null)
        }
    }
}