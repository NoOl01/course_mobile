package com.example.matule.domain.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matule.domain.DaDataClient
import com.example.matule.domain.models.requests.DaDataAddressRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DaDataViewModel : ViewModel() {
    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions = _suggestions.asStateFlow()

    fun getAddresses(query: String) {
        viewModelScope.launch {
            try {
                Log.d("DaDATA", "ok")
                val req = DaDataAddressRequest(query, 5)
                val response = DaDataClient.daDataApi.getAddresses(req)
                Log.d("DaDATA", "$response")
                _suggestions.value = response.suggestions.map { it.value }
            } catch (ex: Exception) {
                Log.e("DaDATA", "Exception in getAddresses", ex)
                _suggestions.value = emptyList()
            }
        }
    }
}
