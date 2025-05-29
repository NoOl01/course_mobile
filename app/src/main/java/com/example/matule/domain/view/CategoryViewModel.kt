package com.example.matule.domain.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matule.domain.RetrofitInstance
import com.example.matule.domain.models.responses.CategoryResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val _categories = MutableStateFlow<CategoryResult?>(null)
    val categories: StateFlow<CategoryResult?> = _categories.asStateFlow()

    fun getAllCategories() {
        viewModelScope.launch {
            try {
                _categories.value = RetrofitInstance.apiService.getAllCategories()
            } catch (ex: Exception) {
                _categories.value = CategoryResult(ex.message, emptyList())
            }
        }
    }
}