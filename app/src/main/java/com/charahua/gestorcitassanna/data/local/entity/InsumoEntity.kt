package com.charahua.gestorcitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "costo_insumos")
data class InsumoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nombre: String,
    val categoria: String,
    val costoUnitario: Double,
    val stock: Int
)
