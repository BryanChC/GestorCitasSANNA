package com.charahua.gestocitassanna.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.charahua.gestocitassanna.data.local.database.SannaDatabase
import com.charahua.gestocitassanna.data.local.entity.*
import com.charahua.gestocitassanna.data.repository.AdminRepository
import com.charahua.gestocitassanna.data.repository.DashboardStats
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AdminViewModel(application: Application) : AndroidViewModel(application) {
    private val db = SannaDatabase.getDatabase(application)
    private val repository = AdminRepository(
        db.sedeDao(),
        db.especialidadDao(),
        db.medicoDao(),
        db.horarioMedicoDao(),
        db.citaDao(),
        db.aseguradoraDao(),
        db.usuarioDao()
    )

    val sedes = repository.getSedesFlow()
    val especialidades = repository.getEspecialidadesFlow()
    val medicos = repository.getMedicosFlow()
    val horarios = repository.getHorariosFlow()
    val citas = repository.getCitasFlow()
    val aseguradoras = repository.getAseguradorasFlow()

    private val _stats = MutableStateFlow(DashboardStats(0, 0, 0))
    val stats: StateFlow<DashboardStats> = _stats.asStateFlow()

    init {
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            _stats.value = repository.getDashboardStats()
        }
    }

    fun addSede(sede: SedeEntity) = viewModelScope.launch { repository.insertSede(sede); loadStats() }
    fun updateSede(sede: SedeEntity) = viewModelScope.launch { repository.updateSede(sede) }
    fun deleteSede(sede: SedeEntity) = viewModelScope.launch { repository.deleteSede(sede); loadStats() }

    fun addEspecialidad(esp: EspecialidadEntity) = viewModelScope.launch { repository.insertEspecialidad(esp) }
    fun updateEspecialidad(esp: EspecialidadEntity) = viewModelScope.launch { repository.updateEspecialidad(esp) }
    fun deleteEspecialidad(esp: EspecialidadEntity) = viewModelScope.launch { repository.deleteEspecialidad(esp) }

    fun addMedico(medico: MedicoEntity) = viewModelScope.launch { repository.insertMedico(medico); loadStats() }
    fun updateMedico(medico: MedicoEntity) = viewModelScope.launch { repository.updateMedico(medico) }
    fun deleteMedico(medico: MedicoEntity) = viewModelScope.launch { repository.deleteMedico(medico); loadStats() }

    fun addHorario(horario: HorarioMedicoEntity) = viewModelScope.launch { repository.insertHorario(horario) }
    fun updateHorario(horario: HorarioMedicoEntity) = viewModelScope.launch { repository.updateHorario(horario) }
    fun deleteHorario(horario: HorarioMedicoEntity) = viewModelScope.launch { repository.deleteHorario(horario) }

    fun updateCitaEstado(citaId: Long, estado: String) = viewModelScope.launch { repository.updateCitaEstado(citaId, estado) }

    fun addAseguradora(aseg: AseguradoraEntity) = viewModelScope.launch { repository.insertAseguradora(aseg) }
    fun updateAseguradora(aseg: AseguradoraEntity) = viewModelScope.launch { repository.updateAseguradora(aseg) }
    fun deleteAseguradora(aseg: AseguradoraEntity) = viewModelScope.launch { repository.deleteAseguradora(aseg) }
}
