package com.charahua.gestocitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "horarios_medicos")
data class HorarioMedicoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val medicoId: Long,
    val sedeId: Long,
    val diaSemana: String,
    val horaInicio: String,
    val horaFin: String,
    val intervaloMinutos: Int = 30
)
