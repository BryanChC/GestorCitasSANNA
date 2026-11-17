package com.charahua.gestorcitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "convenio_coberturas")
data class ConvenioCoberturaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val aseguradoraId: Long,
    val especialidadId: Long,
    val porcentajeCobertura: Int
)
