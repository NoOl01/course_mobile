package com.example.matule.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "app_prefs")

class PreferencesManager(private val context: Context) {
    private val authKey = stringPreferencesKey("auth_info")

    val getAuthData: Flow<Array<String>?> = context.dataStore.data
        .map { preferences ->
            preferences[authKey]?.split(" ")?.let {
                if (it.size == 2) Array(2) { index -> it[index]} else null
            } ?: Array(2) {" "}
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