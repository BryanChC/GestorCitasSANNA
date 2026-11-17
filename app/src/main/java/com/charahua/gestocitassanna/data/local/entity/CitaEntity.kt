package com.charahua.gestocitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "citas")
data class CitaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val pacienteId: Long,
    val medicoId: Long,
    val sedeId: Long,
    val especialidadId: Long,
    val fecha: String,
    val hora: String,
    val estado: String,
    val motivo: String,
    val costo: Double,
    val aseguradoraId: Long? = null,
    val metodoPago: String = "Tarjeta de Crédito",
    val numeroOperacion: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
