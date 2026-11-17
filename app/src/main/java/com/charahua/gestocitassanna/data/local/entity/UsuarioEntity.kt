package com.charahua.gestocitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val email: String,
    val passwordHash: String,
    val salt: String,
    val rol: String,
    val nombre: String,
    val apellido: String,
    val dni: String,
    val telefono: String,
    val createdAt: Long = System.currentTimeMillis()
)
