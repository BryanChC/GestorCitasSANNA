package com.charahua.gestorcitassanna.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.charahua.gestorcitassanna.data.local.database.SannaDatabase
import com.charahua.gestorcitassanna.data.local.entity.PacienteEntity
import com.charahua.gestorcitassanna.data.local.entity.UsuarioEntity
import com.charahua.gestorcitassanna.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PacienteViewModel(application: Application) : AndroidViewModel(application) {
    private val db = SannaDatabase.getDatabase(application)
    private val authRepository = AuthRepository(db)

    private val _usuario = MutableStateFlow<UsuarioEntity?>(null)
    val usuario: StateFlow<UsuarioEntity?> = _usuario.asStateFlow()

    private val _paciente = MutableStateFlow<PacienteEntity?>(null)
    val paciente: StateFlow<PacienteEntity?> = _paciente.asStateFlow()

    fun loadData(userId: Long) {
        viewModelScope.launch {
            val user = authRepository.getUserById(userId)
            _usuario.value = user
            if (user != null) {
                _paciente.value = authRepository.getPacienteByUsuarioId(user.id)
            }
        }
    }

    fun updateProfile(
        userId: Long,
        telefono: String,
        email: String,
        direccion: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            val res = authRepository.updatePatientProfile(userId, telefono, email, direccion)
            res.fold(
                onSuccess = {
                    loadData(userId)
                    onResult(true, null)
                },
                onFailure = { err ->
                    onResult(false, err.localizedMessage)
                }
            )
        }
    }
}
