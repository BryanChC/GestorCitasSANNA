package com.charahua.gestorcitassanna.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.charahua.gestorcitassanna.data.local.database.SannaDatabase
import com.charahua.gestorcitassanna.data.local.entity.*
import com.charahua.gestorcitassanna.data.repository.CitaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CitasViewModel(application: Application) : AndroidViewModel(application) {
    private val db = SannaDatabase.getDatabase(application)
    private val repository = CitaRepository(
        db.citaDao(),
        db.historialReprogramacionDao(),
        db.tarifaDao(),
        db.convenioCoberturaDao(),
        db.sedeDao(),
        db.especialidadDao(),
        db.medicoDao(),
        db.aseguradoraDao()
    )

    private val _sedes = MutableStateFlow<List<SedeEntity>>(emptyList())
    val sedes: StateFlow<List<SedeEntity>> = _sedes.asStateFlow()

    private val _especialidades = MutableStateFlow<List<EspecialidadEntity>>(emptyList())
    val especialidades: StateFlow<List<EspecialidadEntity>> = _especialidades.asStateFlow()

    private val _medicos = MutableStateFlow<List<MedicoEntity>>(emptyList())
    val medicos: StateFlow<List<MedicoEntity>> = _medicos.asStateFlow()

    private val _aseguradoras = MutableStateFlow<List<AseguradoraEntity>>(emptyList())
    val aseguradoras: StateFlow<List<AseguradoraEntity>> = _aseguradoras.asStateFlow()

    // TABLAS INTERMEDIAS PARA EL DIRECTORIO MÉDICO
    private val _medicoEspecialidades = MutableStateFlow<List<MedicoEspecialidadEntity>>(emptyList())
    val medicoEspecialidades: StateFlow<List<MedicoEspecialidadEntity>> = _medicoEspecialidades.asStateFlow()

    private val _medicoSedes = MutableStateFlow<List<MedicoSedeEntity>>(emptyList())
    val medicoSedes: StateFlow<List<MedicoSedeEntity>> = _medicoSedes.asStateFlow()

    private val _tarifaActual = MutableStateFlow<Double>(0.0)
    val tarifaActual: StateFlow<Double> = _tarifaActual.asStateFlow()

    private val _bookingResult = MutableStateFlow<Result<Long>?>(null)
    val bookingResult: StateFlow<Result<Long>?> = _bookingResult.asStateFlow()

    private val _reprogramarResult = MutableStateFlow<Result<Unit>?>(null)
    val reprogramarResult: StateFlow<Result<Unit>?> = _reprogramarResult.asStateFlow()

    init {
        loadBookingData()
    }

    fun loadBookingData() {
        viewModelScope.launch {
            _sedes.value = repository.getSedes()
            _especialidades.value = repository.getEspecialidades()
            _aseguradoras.value = repository.getAseguradoras()
        }
    }

    fun loadAllMedicos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                _medicos.value = db.medicoDao().getAll()

                _medicoEspecialidades.value = db.medicoEspecialidadDao().getAll()
                _medicoSedes.value = db.medicoSedeDao().getAll()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetBookingResult() {
        _bookingResult.value = null
    }

    fun loadMedicosFor(sedeId: Long, espId: Long) {
        viewModelScope.launch {
            _medicos.value = repository.getMedicosForSedeAndEsp(sedeId, espId)
            val tarifa = repository.getTarifa(espId, sedeId)
            _tarifaActual.value = tarifa?.costoBase ?: 120.0
        }
    }

    fun getCitasForPaciente(pacienteId: Long): Flow<List<CitaEntity>> {
        return repository.getCitasForPacienteFlow(pacienteId)
    }
    suspend fun getCitaById(
        citaId: Long
    ): CitaEntity? {

        return repository.getCitaById(citaId)

    }

    fun reservarCita(
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
    ) {
        viewModelScope.launch {
            val result = repository.reservarCita(
                pacienteId,
                medicoId,
                sedeId,
                especialidadId,
                fecha,
                hora,
                motivo,
                aseguradoraId,
                metodoPago,
                numeroOperacion
            )
            _bookingResult.value = result
        }
    }

    fun reprogramarCita(citaId: Long, nuevaFecha: String, nuevaHora: String, motivo: String) {
        viewModelScope.launch {
            val result = repository.reprogramarCita(citaId, nuevaFecha, nuevaHora, motivo)
            _reprogramarResult.value = result
        }
    }

    fun cancelarCita(citaId: Long) {
        viewModelScope.launch {
            repository.actualizarEstadoCita(citaId, "CANCELADA")
        }
    }
}