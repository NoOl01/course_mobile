package com.example.matule.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "app_prefs")

data class AuthData(
    val accessToken: String,
    val refreshToken: String
)

class PreferencesManager(private val context: Context) {
    private val authKey = stringPreferencesKey("auth_info")

    suspend fun getAuthData(): AuthData? {
        val preferences = context.dataStore.data.first()
        return preferences[authKey]?.split(" ")?.takeIf { it.size == 2 }?.let {
            AuthData(
                accessToken = it[0],
                refreshToken = it[1]
            )
        }
    }

    suspend fun saveAuthData(accessToken: String, refreshToken: String){
        val authData = "$accessToken $refreshToken"
        context.dataStore.edit { preferences ->
            preferences[authKey] = authData
        }
    }

    suspend fun deleteAuthData(){
        context.dataStore.edit { preferences ->
            preferences.remove(authKey)
        }
    }
}