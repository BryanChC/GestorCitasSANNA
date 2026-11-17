package com.charahua.gestocitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "costo_tiempo_productivo")
data class TiempoProductivoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val descripcion: String,
    val horasMensuales: Double,
    val costoHora: Double
)
