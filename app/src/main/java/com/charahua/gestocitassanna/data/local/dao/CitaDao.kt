package com.charahua.gestocitassanna.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.charahua.gestocitassanna.data.local.entity.CitaEntity
import com.charahua.gestocitassanna.data.local.entity.HistorialReprogramacionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CitaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cita: CitaEntity): Long

    @Update
    suspend fun update(cita: CitaEntity)

    @Delete
    suspend fun delete(cita: CitaEntity)

    @Query("SELECT * FROM citas WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): CitaEntity?

    @Query("SELECT * FROM citas WHERE pacienteId = :pacienteId ORDER BY fecha DESC, hora DESC")
    fun getCitasForPacienteFlow(pacienteId: Long): Flow<List<CitaEntity>>

    @Query("SELECT * FROM citas WHERE medicoId = :medicoId ORDER BY fecha DESC, hora DESC")
    fun getCitasForMedicoFlow(medicoId: Long): Flow<List<CitaEntity>>

    @Query("SELECT * FROM citas ORDER BY fecha DESC, hora DESC")
    fun getAllFlow(): Flow<List<CitaEntity>>

    @Query("SELECT * FROM citas")
    suspend fun getAll(): List<CitaEntity>

    @Query("SELECT * FROM citas WHERE medicoId = :medicoId AND fecha = :fecha AND hora = :hora AND estado != 'CANCELADA'")
    suspend fun getConflictingCita(medicoId: Long, fecha: String, hora: String): CitaEntity?
}

@Dao
interface HistorialReprogramacionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historial: HistorialReprogramacionEntity): Long

    @Query("SELECT * FROM historial_reprogramaciones WHERE citaId = :citaId ORDER BY fechaReprogramacion DESC")
    fun getHistorialForCitaFlow(citaId: Long): Flow<List<HistorialReprogramacionEntity>>
}
