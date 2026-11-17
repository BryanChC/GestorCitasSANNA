package com.charahua.gestorcitassanna.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.charahua.gestorcitassanna.data.local.entity.PacienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PacienteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(paciente: PacienteEntity): Long

    @Update
    suspend fun update(paciente: PacienteEntity)

    @Query("UPDATE pacientes SET direccion = :direccion WHERE usuarioId = :usuarioId")
    suspend fun updateDireccion(usuarioId: Long, direccion: String)

    @Query("SELECT * FROM pacientes WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): PacienteEntity?

    @Query("SELECT * FROM pacientes WHERE usuarioId = :usuarioId LIMIT 1")
    suspend fun getByUsuarioId(usuarioId: Long): PacienteEntity?

    @Query("SELECT * FROM pacientes WHERE usuarioId = :usuarioId LIMIT 1")
    fun getByUsuarioIdFlow(usuarioId: Long): Flow<PacienteEntity?>
}
