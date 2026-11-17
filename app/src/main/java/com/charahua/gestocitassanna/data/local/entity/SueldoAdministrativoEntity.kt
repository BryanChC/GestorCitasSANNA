package com.charahua.gestocitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sueldos_administrativos")
data class SueldoAdministrativoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val puesto: String,
    val departamento: String,
    val sueldo: Double
)
