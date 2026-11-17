package com.charahua.gestocitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tarifas")
data class TarifaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val especialidadId: Long,
    val sedeId: Long,
    val costoBase: Double
)
