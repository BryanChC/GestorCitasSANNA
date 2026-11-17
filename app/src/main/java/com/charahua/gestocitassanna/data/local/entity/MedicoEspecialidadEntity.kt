package com.charahua.gestocitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medico_especialidades")
data class MedicoEspecialidadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val medicoId: Long,
    val especialidadId: Long
)
