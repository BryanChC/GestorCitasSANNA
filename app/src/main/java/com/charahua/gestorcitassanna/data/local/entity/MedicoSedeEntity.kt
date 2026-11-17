package com.charahua.gestorcitassanna.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medico_sedes")
data class MedicoSedeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val medicoId: Long,
    val sedeId: Long
)
