package com.charahua.gestocitassanna.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.charahua.gestocitassanna.data.local.entity.AseguradoraEntity
import com.charahua.gestocitassanna.data.local.entity.ConvenioCoberturaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AseguradoraDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(aseguradora: AseguradoraEntity): Long

    @Update
    suspend fun update(aseguradora: AseguradoraEntity)

    @Delete
    suspend fun delete(aseguradora: AseguradoraEntity)

    @Query("SELECT * FROM aseguradoras")
    fun getAllFlow(): Flow<List<AseguradoraEntity>>

    @Query("SELECT * FROM aseguradoras")
    suspend fun getAll(): List<AseguradoraEntity>

    @Query("SELECT * FROM aseguradoras WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): AseguradoraEntity?
}

@Dao
interface ConvenioCoberturaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(convenio: ConvenioCoberturaEntity): Long

    @Query("SELECT * FROM convenio_coberturas WHERE aseguradoraId = :aseguradoraId AND especialidadId = :especialidadId LIMIT 1")
    suspend fun getCobertura(aseguradoraId: Long, especialidadId: Long): ConvenioCoberturaEntity?

    @Query("SELECT * FROM convenio_coberturas")
    fun getAllFlow(): Flow<List<ConvenioCoberturaEntity>>
}
