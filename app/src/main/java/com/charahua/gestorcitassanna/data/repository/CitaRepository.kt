package com.charahua.gestorcitassanna.data.repository

import com.charahua.gestorcitassanna.data.local.dao.*
import com.charahua.gestorcitassanna.data.local.entity.CitaEntity
import com.charahua.gestorcitassanna.data.local.entity.HistorialReprogramacionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CitaRepository(
    private val citaDao: CitaDao,
    private val historialDao: HistorialReprogramacionDao,
    private val tarifaDao: TarifaDao,
    private val convenioDao: ConvenioCoberturaDao,
    private val sedeDao: SedeDao,
    private val especialidadDao: EspecialidadDao,
    private val medicoDao: MedicoDao,
    private val aseguradoraDao: AseguradoraDao
) {
    fun getCitasForPacienteFlow(pacienteId: Long): Flow<List<CitaEntity>> {
        return citaDao.getCitasForPacienteFlow(pacienteId)
    }

    fun getAllCitasFlow(): Flow<List<CitaEntity>> {
        return citaDao.getAllFlow()
    }

    suspend fun reservarCita(
        pacienteId: Long,
        medicoId: Long,
        sedeId: Long,
        especialidadId: Long,
        fecha: String,
        hora: String,
        motivo: String,
        aseguradoraId: Long?,
        metodoPago: String = "Tarjeta de Crédito",
        numeroOperacion: String = ""
    ): Result<Long> = withContext(Dispatchers.IO) {
        val conflict = citaDao.getConflictingCita(medicoId, fecha, hora)
        if (conflict != null) {
            return@withContext Result.failure(Exception("El médico ya tiene una cita programada en este horario."))
        }

        val tarifa = tarifaDao.getTarifa(especialidadId, sedeId)?.costoBase ?: 120.0
        var costoFinal = tarifa
        if (aseguradoraId != null) {
            val cobertura = convenioDao.getCobertura(aseguradoraId, especialidadId)?.porcentajeCobertura ?: 0
            costoFinal = tarifa * (1.0 - (cobertura / 100.0))
        }

        val cita = CitaEntity(
            pacienteId = pacienteId,
            medicoId = medicoId,
            sedeId = sedeId,
            especialidadId = especialidadId,
            fecha = fecha,
            hora = hora,
            estado = "PROGRAMADA",
            motivo = motivo,
            costo = costoFinal,
            aseguradoraId = aseguradoraId,
            metodoPago = metodoPago,
            numeroOperacion = numeroOperacion.ifBlank { "TXN-${System.currentTimeMillis().toString().takeLast(8)}" }
        )
        val id = citaDao.insert(cita)
        Result.success(id)
    }

    suspend fun actualizarEstadoCita(citaId: Long, nuevoEstado: String) = withContext(Dispatchers.IO) {
        val cita = citaDao.getById(citaId) ?: return@withContext
        citaDao.update(cita.copy(estado = nuevoEstado))
    }

    suspend fun reprogramarCita(citaId: Long, nuevaFecha: String, nuevaHora: String, motivo: String): Result<Unit> = withContext(Dispatchers.IO) {
        val cita = citaDao.getById(citaId) ?: return@withContext Result.failure(Exception("Cita no encontrada."))

        val conflict = citaDao.getConflictingCita(cita.medicoId, nuevaFecha, nuevaHora)
        if (conflict != null && conflict.id != citaId) {
            return@withContext Result.failure(Exception("El médico ya tiene una cita en el nuevo horario seleccionado."))
        }

        historialDao.insert(
            HistorialReprogramacionEntity(
                citaId = citaId,
                fechaAnterior = cita.fecha,
                horaAnterior = cita.hora,
                fechaNueva = nuevaFecha,
                horaNueva = nuevaHora,
                motivo = motivo
            )
        )

        citaDao.update(
            cita.copy(
                fecha = nuevaFecha,
                hora = nuevaHora,
                estado = "REPROGRAMADA"
            )
        )
        Result.success(Unit)
    }

    fun getHistorialForCitaFlow(citaId: Long): Flow<List<HistorialReprogramacionEntity>> {
        return historialDao.getHistorialForCitaFlow(citaId)
    }

    suspend fun getSedes() = withContext(Dispatchers.IO) { sedeDao.getAll() }
    suspend fun getEspecialidades() = withContext(Dispatchers.IO) { especialidadDao.getAll() }
    suspend fun getMedicosForSedeAndEsp(sedeId: Long, espId: Long) = withContext(Dispatchers.IO) { medicoDao.getMedicosForSedeAndEspecialidad(sedeId, espId) }
    suspend fun getAseguradoras() = withContext(Dispatchers.IO) { aseguradoraDao.getAll() }
    suspend fun getTarifa(espId: Long, sedeId: Long) = withContext(Dispatchers.IO) { tarifaDao.getTarifa(espId, sedeId) }
    suspend fun getCitaById(id: Long) = withContext(Dispatchers.IO) { citaDao.getById(id) }
    suspend fun getSedeById(id: Long) = withContext(Dispatchers.IO) { sedeDao.getById(id) }
    suspend fun getEspecialidadById(id: Long) = withContext(Dispatchers.IO) { especialidadDao.getById(id) }
    suspend fun getMedicoById(id: Long) = withContext(Dispatchers.IO) { medicoDao.getById(id) }
    suspend fun getAseguradoraById(id: Long?) = withContext(Dispatchers.IO) { id?.let { aseguradoraDao.getById(it) } }
}
