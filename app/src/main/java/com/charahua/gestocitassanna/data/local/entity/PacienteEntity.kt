package com.charahua.gestocitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pacientes")
data class PacienteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val usuarioId: Long,
    val fechaNacimiento: String,
    val genero: String,
    val direccion: String,
    val aseguradoraId: Long? = null
)
