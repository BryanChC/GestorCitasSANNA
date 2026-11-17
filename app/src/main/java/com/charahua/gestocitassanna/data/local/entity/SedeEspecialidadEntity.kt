package com.charahua.gestocitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sede_especialidades")
data class SedeEspecialidadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val sedeId: Long,
    val especialidadId: Long
)
