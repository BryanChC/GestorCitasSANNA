package com.charahua.gestocitassanna.data.repository

import com.charahua.gestocitassanna.data.local.dao.*
import com.charahua.gestocitassanna.data.local.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AdminRepository(
    private val sedeDao: SedeDao,
    private val especialidadDao: EspecialidadDao,
    private val medicoDao: MedicoDao,
    private val horarioDao: HorarioMedicoDao,
    private val citaDao: CitaDao,
    private val aseguradoraDao: AseguradoraDao,
    private val usuarioDao: UsuarioDao
) {
    fun getSedesFlow() = sedeDao.getAllFlow()
    suspend fun insertSede(sede: SedeEntity) = withContext(Dispatchers.IO) { sedeDao.insert(sede) }
    suspend fun updateSede(sede: SedeEntity) = withContext(Dispatchers.IO) { sedeDao.update(sede) }
    suspend fun deleteSede(sede: SedeEntity) = withContext(Dispatchers.IO) { sedeDao.delete(sede) }

    fun getEspecialidadesFlow() = especialidadDao.getAllFlow()
    suspend fun insertEspecialidad(esp: EspecialidadEntity) = withContext(Dispatchers.IO) { especialidadDao.insert(esp) }
    suspend fun updateEspecialidad(esp: EspecialidadEntity) = withContext(Dispatchers.IO) { especialidadDao.update(esp) }
    suspend fun deleteEspecialidad(esp: EspecialidadEntity) = withContext(Dispatchers.IO) { especialidadDao.delete(esp) }

    fun getMedicosFlow() = medicoDao.getAllFlow()
    suspend fun insertMedico(medico: MedicoEntity) = withContext(Dispatchers.IO) { medicoDao.insert(medico) }
    suspend fun updateMedico(medico: MedicoEntity) = withContext(Dispatchers.IO) { medicoDao.update(medico) }
    suspend fun deleteMedico(medico: MedicoEntity) = withContext(Dispatchers.IO) { medicoDao.delete(medico) }

    fun getHorariosFlow() = horarioDao.getAllFlow()
    suspend fun insertHorario(horario: HorarioMedicoEntity) = withContext(Dispatchers.IO) { horarioDao.insert(horario) }
    suspend fun updateHorario(horario: HorarioMedicoEntity) = withContext(Dispatchers.IO) { horarioDao.update(horario) }
    suspend fun deleteHorario(horario: HorarioMedicoEntity) = withContext(Dispatchers.IO) { horarioDao.delete(horario) }

    fun getCitasFlow() = citaDao.getAllFlow()
    suspend fun updateCitaEstado(citaId: Long, estado: String) = withContext(Dispatchers.IO) {
        val cita = citaDao.getById(citaId) ?: return@withContext
        citaDao.update(cita.copy(estado = estado))
    }

    fun getAseguradorasFlow() = aseguradoraDao.getAllFlow()
    suspend fun insertAseguradora(aseg: AseguradoraEntity) = withContext(Dispatchers.IO) { aseguradoraDao.insert(aseg) }
    suspend fun updateAseguradora(aseg: AseguradoraEntity) = withContext(Dispatchers.IO) { aseguradoraDao.update(aseg) }
    suspend fun deleteAseguradora(aseg: AseguradoraEntity) = withContext(Dispatchers.IO) { aseguradoraDao.delete(aseg) }

    suspend fun getDashboardStats(): DashboardStats = withContext(Dispatchers.IO) {
        val sedes = sedeDao.getAll().size
        val medicos = medicoDao.getAll().size
        val citas = citaDao.getAll().size
        DashboardStats(sedesCount = sedes, medicosCount = medicos, citasCount = citas)
    }
}

data class DashboardStats(
    val sedesCount: Int,
    val medicosCount: Int,
    val citasCount: Int
)
