package com.charahua.gestocitassanna.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.charahua.gestocitassanna.data.local.entity.TarifaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TarifaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tarifa: TarifaEntity): Long

    @Update
    suspend fun update(tarifa: TarifaEntity)

    @Delete
    suspend fun delete(tarifa: TarifaEntity)

    @Query("SELECT * FROM tarifas WHERE especialidadId = :especialidadId AND sedeId = :sedeId LIMIT 1")
    suspend fun getTarifa(especialidadId: Long, sedeId: Long): TarifaEntity?

    @Query("SELECT * FROM tarifas")
    fun getAllFlow(): Flow<List<TarifaEntity>>
}
