package com.charahua.gestorcitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicos")
data class MedicoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nombre: String,
    val apellido: String,
    val colegiatura: String,
    val telefono: String,
    val email: String,
    val fotoUrl: String = ""
)
