package com.charahua.gestorcitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sedes")
data class SedeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nombre: String,
    val direccion: String,
    val distrito: String,
    val telefono: String,
    val imagenUrl: String = ""
)
