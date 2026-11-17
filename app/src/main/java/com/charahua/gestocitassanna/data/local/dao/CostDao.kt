package com.charahua.gestocitassanna.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.charahua.gestocitassanna.data.local.entity.CostoSalarialDirectoEntity
import com.charahua.gestocitassanna.data.local.entity.GastoOperativoEntity
import com.charahua.gestocitassanna.data.local.entity.InsumoEntity
import com.charahua.gestocitassanna.data.local.entity.MaterialEntity
import com.charahua.gestocitassanna.data.local.entity.SueldoAdministrativoEntity
import com.charahua.gestocitassanna.data.local.entity.TiempoProductivoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InsumoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: InsumoEntity): Long

    @Update
    suspend fun update(item: InsumoEntity)

    @Delete
    suspend fun delete(item: InsumoEntity)

    @Query("SELECT * FROM costo_insumos")
    fun getAllFlow(): Flow<List<InsumoEntity>>

    @Query("SELECT * FROM costo_insumos")
    suspend fun getAll(): List<InsumoEntity>
}

@Dao
interface MaterialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MaterialEntity): Long

    @Update
    suspend fun update(item: MaterialEntity)

    @Delete
    suspend fun delete(item: MaterialEntity)

    @Query("SELECT * FROM costo_materiales")
    fun getAllFlow(): Flow<List<MaterialEntity>>

    @Query("SELECT * FROM costo_materiales")
    suspend fun getAll(): List<MaterialEntity>
}

@Dao
interface TiempoProductivoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TiempoProductivoEntity): Long

    @Update
    suspend fun update(item: TiempoProductivoEntity)

    @Delete
    suspend fun delete(item: TiempoProductivoEntity)

    @Query("SELECT * FROM costo_tiempo_productivo")
    fun getAllFlow(): Flow<List<TiempoProductivoEntity>>

    @Query("SELECT * FROM costo_tiempo_productivo")
    suspend fun getAll(): List<TiempoProductivoEntity>
}

@Dao
interface CostoSalarialDirectoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CostoSalarialDirectoEntity): Long

    @Update
    suspend fun update(item: CostoSalarialDirectoEntity)

    @Delete
    suspend fun delete(item: CostoSalarialDirectoEntity)

    @Query("SELECT * FROM costo_salarial_directo")
    fun getAllFlow(): Flow<List<CostoSalarialDirectoEntity>>

    @Query("SELECT * FROM costo_salarial_directo")
    suspend fun getAll(): List<CostoSalarialDirectoEntity>
}

@Dao
interface GastoOperativoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GastoOperativoEntity): Long

    @Update
    suspend fun update(item: GastoOperativoEntity)

    @Delete
    suspend fun delete(item: GastoOperativoEntity)

    @Query("SELECT * FROM gastos_operativos")
    fun getAllFlow(): Flow<List<GastoOperativoEntity>>

    @Query("SELECT * FROM gastos_operativos")
    suspend fun getAll(): List<GastoOperativoEntity>
}

@Dao
interface SueldoAdministrativoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SueldoAdministrativoEntity): Long

    @Update
    suspend fun update(item: SueldoAdministrativoEntity)

    @Delete
    suspend fun delete(item: SueldoAdministrativoEntity)

    @Query("SELECT * FROM sueldos_administrativos")
    fun getAllFlow(): Flow<List<SueldoAdministrativoEntity>>

    @Query("SELECT * FROM sueldos_administrativos")
    suspend fun getAll(): List<SueldoAdministrativoEntity>
}
