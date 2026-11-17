package com.charahua.gestocitassanna.data.repository

import com.charahua.gestocitassanna.data.local.dao.*
import com.charahua.gestocitassanna.data.local.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CostosRepository(
    private val insumoDao: InsumoDao,
    private val materialDao: MaterialDao,
    private val tiempoDao: TiempoProductivoDao,
    private val salarialDao: CostoSalarialDirectoDao,
    private val operativoDao: GastoOperativoDao,
    private val administrativoDao: SueldoAdministrativoDao
) {
    fun getInsumosFlow() = insumoDao.getAllFlow()
    suspend fun insertInsumo(item: InsumoEntity) = withContext(Dispatchers.IO) { insumoDao.insert(item) }
    suspend fun updateInsumo(item: InsumoEntity) = withContext(Dispatchers.IO) { insumoDao.update(item) }
    suspend fun deleteInsumo(item: InsumoEntity) = withContext(Dispatchers.IO) { insumoDao.delete(item) }

    fun getMaterialesFlow() = materialDao.getAllFlow()
    suspend fun insertMaterial(item: MaterialEntity) = withContext(Dispatchers.IO) { materialDao.insert(item) }
    suspend fun updateMaterial(item: MaterialEntity) = withContext(Dispatchers.IO) { materialDao.update(item) }
    suspend fun deleteMaterial(item: MaterialEntity) = withContext(Dispatchers.IO) { materialDao.delete(item) }

    fun getTiempoProductivoFlow() = tiempoDao.getAllFlow()
    suspend fun insertTiempo(item: TiempoProductivoEntity) = withContext(Dispatchers.IO) { tiempoDao.insert(item) }
    suspend fun updateTiempo(item: TiempoProductivoEntity) = withContext(Dispatchers.IO) { tiempoDao.update(item) }
    suspend fun deleteTiempo(item: TiempoProductivoEntity) = withContext(Dispatchers.IO) { tiempoDao.delete(item) }

    fun getSalarialDirectoFlow() = salarialDao.getAllFlow()
    suspend fun insertSalarial(item: CostoSalarialDirectoEntity) = withContext(Dispatchers.IO) { salarialDao.insert(item) }
    suspend fun updateSalarial(item: CostoSalarialDirectoEntity) = withContext(Dispatchers.IO) { salarialDao.update(item) }
    suspend fun deleteSalarial(item: CostoSalarialDirectoEntity) = withContext(Dispatchers.IO) { salarialDao.delete(item) }

    fun getGastosOperativosFlow() = operativoDao.getAllFlow()
    suspend fun insertGasto(item: GastoOperativoEntity) = withContext(Dispatchers.IO) { operativoDao.insert(item) }
    suspend fun updateGasto(item: GastoOperativoEntity) = withContext(Dispatchers.IO) { operativoDao.update(item) }
    suspend fun deleteGasto(item: GastoOperativoEntity) = withContext(Dispatchers.IO) { operativoDao.delete(item) }

    fun getSueldosAdministrativosFlow() = administrativoDao.getAllFlow()
    suspend fun insertAdminSueldo(item: SueldoAdministrativoEntity) = withContext(Dispatchers.IO) { administrativoDao.insert(item) }
    suspend fun updateAdminSueldo(item: SueldoAdministrativoEntity) = withContext(Dispatchers.IO) { administrativoDao.update(item) }
    suspend fun deleteAdminSueldo(item: SueldoAdministrativoEntity) = withContext(Dispatchers.IO) { administrativoDao.delete(item) }
}
