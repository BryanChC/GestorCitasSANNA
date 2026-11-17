package com.charahua.gestorcitassanna.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.charahua.gestorcitassanna.data.local.database.SannaDatabase
import com.charahua.gestorcitassanna.data.local.entity.*
import com.charahua.gestorcitassanna.data.repository.CostosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CostosViewModel(application: Application) : AndroidViewModel(application) {
    private val db = SannaDatabase.getDatabase(application)
    private val repository = CostosRepository(
        db.insumoDao(),
        db.materialDao(),
        db.tiempoProductivoDao(),
        db.costoSalarialDirectoDao(),
        db.gastoOperativoDao(),
        db.sueldoAdministrativoDao()
    )

    val insumos = repository.getInsumosFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val materiales = repository.getMaterialesFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val tiempoProductivo = repository.getTiempoProductivoFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val costoSalarial = repository.getSalarialDirectoFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val gastosOperativos = repository.getGastosOperativosFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val sueldosAdmin = repository.getSueldosAdministrativosFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addInsumo(item: InsumoEntity) = viewModelScope.launch { repository.insertInsumo(item) }
    fun updateInsumo(item: InsumoEntity) = viewModelScope.launch { repository.updateInsumo(item) }
    fun deleteInsumo(item: InsumoEntity) = viewModelScope.launch { repository.deleteInsumo(item) }

    fun addMaterial(item: MaterialEntity) = viewModelScope.launch { repository.insertMaterial(item) }
    fun updateMaterial(item: MaterialEntity) = viewModelScope.launch { repository.updateMaterial(item) }
    fun deleteMaterial(item: MaterialEntity) = viewModelScope.launch { repository.deleteMaterial(item) }

    fun addTiempo(item: TiempoProductivoEntity) = viewModelScope.launch { repository.insertTiempo(item) }
    fun updateTiempo(item: TiempoProductivoEntity) = viewModelScope.launch { repository.updateTiempo(item) }
    fun deleteTiempo(item: TiempoProductivoEntity) = viewModelScope.launch { repository.deleteTiempo(item) }

    fun addSalarial(item: CostoSalarialDirectoEntity) = viewModelScope.launch { repository.insertSalarial(item) }
    fun updateSalarial(item: CostoSalarialDirectoEntity) = viewModelScope.launch { repository.updateSalarial(item) }
    fun deleteSalarial(item: CostoSalarialDirectoEntity) = viewModelScope.launch { repository.deleteSalarial(item) }

    fun addGasto(item: GastoOperativoEntity) = viewModelScope.launch { repository.insertGasto(item) }
    fun updateGasto(item: GastoOperativoEntity) = viewModelScope.launch { repository.updateGasto(item) }
    fun deleteGasto(item: GastoOperativoEntity) = viewModelScope.launch { repository.deleteGasto(item) }

    fun addSueldoAdmin(item: SueldoAdministrativoEntity) = viewModelScope.launch { repository.insertAdminSueldo(item) }
    fun updateSueldoAdmin(item: SueldoAdministrativoEntity) = viewModelScope.launch { repository.updateAdminSueldo(item) }
    fun deleteSueldoAdmin(item: SueldoAdministrativoEntity) = viewModelScope.launch { repository.deleteAdminSueldo(item) }
}
