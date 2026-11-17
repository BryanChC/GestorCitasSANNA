package com.charahua.gestorcitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "especialidades")
data class EspecialidadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nombre: String,
    val descripcion: String,
    val icono: String = ""
)
