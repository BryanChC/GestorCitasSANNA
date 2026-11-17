package com.charahua.gestocitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gastos_operativos")
data class GastoOperativoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val concepto: String,
    val tipoGasto: String,
    val montoMensual: Double
)
