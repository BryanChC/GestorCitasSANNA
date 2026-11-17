package com.charahua.gestocitassanna.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.charahua.gestocitassanna.data.local.database.SannaDatabase
import com.charahua.gestocitassanna.data.repository.AuthRepository
import com.charahua.gestocitassanna.utils.SessionManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val db = SannaDatabase.getDatabase(application)
    private val repository = AuthRepository(db)
    private val sessionManager = SessionManager(application)

    val userIdFlow = sessionManager.userIdFlow
    val pacienteIdFlow = sessionManager.pacienteIdFlow
    val userRolFlow = sessionManager.userRolFlow
    val isFirstLaunchFlow = sessionManager.isFirstLaunchFlow

    private val _uiState = MutableStateFlow<AuthState>(AuthState.Idle)
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            val user = repository.login(email, pass)
            if (user != null) {
                val paciente = repository.getPacienteByUsuarioId(user.id)
                val pacienteId = paciente?.id ?: 1L
                sessionManager.saveSession(user.id, pacienteId, user.email, user.rol)
                _uiState.value = AuthState.Success(user.rol, user.id, pacienteId)
            } else {
                _uiState.value = AuthState.Error("Correo o contraseña incorrectos.")
            }
        }
    }

    fun register(
        nombre: String,
        apellido: String,
        email: String,
        dni: String,
        telefono: String,
        pass: String,
        fnac: String,
        gen: String,
        dir: String
    ) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            val result = repository.registerPatient(nombre, apellido, email, dni, telefono, pass, fnac, gen, dir)
            result.fold(
                onSuccess = { (user, pacienteId) ->
                    sessionManager.saveSession(user.id, pacienteId, user.email, user.rol)
                    _uiState.value = AuthState.Success(user.rol, user.id, pacienteId)
                },
                onFailure = { err ->
                    _uiState.value = AuthState.Error(err.localizedMessage ?: "Error en el registro.")
                }
            )
        }
    }

    fun completeFirstLaunch() {
        viewModelScope.launch {
            sessionManager.setFirstLaunchCompleted()
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _uiState.value = AuthState.Idle
        }
    }
}

sealed interface AuthState {
    object Idle : AuthState
    object Loading : AuthState
    data class Success(val rol: String, val userId: Long, val pacienteId: Long) : AuthState
    data class Error(val message: String) : AuthState
}
