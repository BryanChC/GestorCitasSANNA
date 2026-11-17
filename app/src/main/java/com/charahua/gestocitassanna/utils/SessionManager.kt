package com.charahua.gestocitassanna.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sanna_session")

class SessionManager(private val context: Context) {
    companion object {
        val USER_ID_KEY = longPreferencesKey("user_id")
        val PACIENTE_ID_KEY = longPreferencesKey("paciente_id")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        val USER_ROL_KEY = stringPreferencesKey("user_rol")
        val IS_FIRST_LAUNCH_KEY = booleanPreferencesKey("is_first_launch")
    }

    val userIdFlow: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY] ?: -1L
    }

    val pacienteIdFlow: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[PACIENTE_ID_KEY] ?: 1L
    }

    val userRolFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_ROL_KEY] ?: ""
    }

    val isFirstLaunchFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_FIRST_LAUNCH_KEY] ?: true
    }

    suspend fun saveSession(userId: Long, pacienteId: Long, email: String, rol: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[PACIENTE_ID_KEY] = pacienteId
            preferences[USER_EMAIL_KEY] = email
            preferences[USER_ROL_KEY] = rol
        }
    }

    suspend fun setFirstLaunchCompleted() {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_LAUNCH_KEY] = false
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = -1L
            preferences[PACIENTE_ID_KEY] = -1L
            preferences[USER_EMAIL_KEY] = ""
            preferences[USER_ROL_KEY] = ""
        }
    }
}
