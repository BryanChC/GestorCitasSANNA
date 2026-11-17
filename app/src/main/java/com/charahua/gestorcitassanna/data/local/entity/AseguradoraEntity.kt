package com.charahua.gestorcitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "aseguradoras")
data class AseguradoraEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nombre: String,
    val ruc: String,
    val contacto: String
)
