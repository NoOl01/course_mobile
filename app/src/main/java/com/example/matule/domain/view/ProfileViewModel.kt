package com.example.matule.domain.view

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.RetrofitInstance
import com.example.matule.domain.models.responses.ErrorResult
import com.example.matule.domain.models.responses.ProfileInfoResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileViewModel : ViewModel() {
    private val _profile = MutableStateFlow<ProfileInfoResult?>(null)
    val profile: StateFlow<ProfileInfoResult?> = _profile.asStateFlow()

    fun getProfileInfo(
        preferencesManager: PreferencesManager
    ) {
        viewModelScope.launch {
            try {
                val token = preferencesManager.getAuthData()
                if (token != null) {
                    _profile.value =
                        RetrofitInstance.apiService.getProfileInfo("Bearer ${token.accessToken}")
                }
            } catch (ex: Exception) {
                _profile.value = ProfileInfoResult(ex.message, null)
            }
        }
    }

    private fun prepareAvatarFromUri(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Не удалось получить поток из URI")

        val file = File(context.cacheDir, "avatar.png")
        file.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        return file
    }

    suspend fun updateProfileAvatar(
        context: Context,
        preferencesManager: PreferencesManager,
        uri: Uri
    ) {
        try {
            val token = preferencesManager.getAuthData()
            if (token != null) {
                val file = prepareAvatarFromUri(context, uri)

                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("avatar", file.name, requestFile)
                val response = RetrofitInstance.apiService.updateProfileAvatar("Bearer ${token.accessToken}", body)

                ErrorResult(response.error)
            } else {
                ErrorResult("Unauthorized")
            }
        } catch (ex: Exception) {
            ErrorResult(ex.message!!)
        }
    }
}