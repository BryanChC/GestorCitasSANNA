package com.charahua.gestorcitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "costo_salarial_directo")
data class CostoSalarialDirectoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val cargo: String,
    val sueldoBase: Double,
    val beneficiosSociales: Double,
    val total: Double
)
