package com.charahua.gestorcitassanna.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.charahua.gestorcitassanna.data.local.entity.EspecialidadEntity
import com.charahua.gestorcitassanna.data.local.entity.SedeEntity
import com.charahua.gestorcitassanna.data.local.entity.SedeEspecialidadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SedeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sede: SedeEntity): Long

    @Update
    suspend fun update(sede: SedeEntity)

    @Delete
    suspend fun delete(sede: SedeEntity)

    @Query("SELECT * FROM sedes")
    fun getAllFlow(): Flow<List<SedeEntity>>

    @Query("SELECT * FROM sedes")
    suspend fun getAll(): List<SedeEntity>

    @Query("SELECT * FROM sedes WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): SedeEntity?
}

@Dao
interface EspecialidadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(especialidad: EspecialidadEntity): Long

    @Update
    suspend fun update(especialidad: EspecialidadEntity)

    @Delete
    suspend fun delete(especialidad: EspecialidadEntity)

    @Query("SELECT * FROM especialidades")
    fun getAllFlow(): Flow<List<EspecialidadEntity>>

    @Query("SELECT * FROM especialidades")
    suspend fun getAll(): List<EspecialidadEntity>

    @Query("SELECT * FROM especialidades WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): EspecialidadEntity?

    @Query("""
        SELECT e.* FROM especialidades e
        INNER JOIN sede_especialidades se ON e.id = se.especialidadId
        WHERE se.sedeId = :sedeId
    """)
    fun getForSedeFlow(sedeId: Long): Flow<List<EspecialidadEntity>>
}

@Dao
interface SedeEspecialidadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SedeEspecialidadEntity): Long
}
