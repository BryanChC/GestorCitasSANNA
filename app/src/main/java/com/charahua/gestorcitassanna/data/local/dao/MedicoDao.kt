package com.charahua.gestorcitassanna.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.charahua.gestorcitassanna.data.local.entity.HorarioMedicoEntity
import com.charahua.gestorcitassanna.data.local.entity.MedicoEntity
import com.charahua.gestorcitassanna.data.local.entity.MedicoEspecialidadEntity
import com.charahua.gestorcitassanna.data.local.entity.MedicoSedeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medico: MedicoEntity): Long

    @Update
    suspend fun update(medico: MedicoEntity)

    @Delete
    suspend fun delete(medico: MedicoEntity)

    @Query("SELECT * FROM medicos")
    fun getAllFlow(): Flow<List<MedicoEntity>>

    @Query("SELECT * FROM medicos")
    suspend fun getAll(): List<MedicoEntity>

    @Query("SELECT * FROM medicos WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): MedicoEntity?

    @Query("""
        SELECT m.* FROM medicos m
        INNER JOIN medico_sedes ms ON m.id = ms.medicoId
        INNER JOIN medico_especialidades me ON m.id = me.medicoId
        WHERE ms.sedeId = :sedeId AND me.especialidadId = :especialidadId
    """)
    fun getMedicosForSedeAndEspecialidadFlow(sedeId: Long, especialidadId: Long): Flow<List<MedicoEntity>>

    @Query("""
        SELECT m.* FROM medicos m
        INNER JOIN medico_sedes ms ON m.id = ms.medicoId
        INNER JOIN medico_especialidades me ON m.id = me.medicoId
        WHERE ms.sedeId = :sedeId AND me.especialidadId = :especialidadId
    """)
    suspend fun getMedicosForSedeAndEspecialidad(sedeId: Long, especialidadId: Long): List<MedicoEntity>
}

@Dao
interface MedicoSedeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MedicoSedeEntity): Long


    @Query("SELECT * FROM medico_sedes")
    suspend fun getAll(): List<MedicoSedeEntity>
}

@Dao
interface MedicoEspecialidadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MedicoEspecialidadEntity): Long


    @Query("SELECT * FROM medico_especialidades")
    suspend fun getAll(): List<MedicoEspecialidadEntity>
}

@Dao
interface HorarioMedicoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(horario: HorarioMedicoEntity): Long

    @Update
    suspend fun update(horario: HorarioMedicoEntity)

    @Delete
    suspend fun delete(horario: HorarioMedicoEntity)

    @Query("SELECT * FROM horarios_medicos WHERE medicoId = :medicoId AND sedeId = :sedeId")
    suspend fun getHorarios(medicoId: Long, sedeId: Long): List<HorarioMedicoEntity>

    @Query("SELECT * FROM horarios_medicos")
    fun getAllFlow(): Flow<List<HorarioMedicoEntity>>
}