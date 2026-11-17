package com.charahua.gestorcitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historial_reprogramaciones")
data class HistorialReprogramacionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val citaId: Long,
    val fechaAnterior: String,
    val horaAnterior: String,
    val fechaNueva: String,
    val horaNueva: String,
    val motivo: String,
    val fechaReprogramacion: Long = System.currentTimeMillis()
)
