package com.charahua.gestorcitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "costo_materiales")
data class MaterialEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nombre: String,
    val tipo: String,
    val costo: Double,
    val vidaUtilMeses: Int
)
