package com.charahua.gestorcitassanna.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TarjetaMedicoSanna(
    nombreMedico: String,
    especialidad: String,
    cmp: String,
    horariosDisponibles: List<String>,
    horarioSeleccionado: String?,
    onHorarioSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Surface(
                    shape = CircleShape,
                    color = Color(0xFFF3F4F6),
                    modifier = Modifier.size(54.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Avatar Médico",
                        tint = Color(0xFF002855), // Azul SANNA
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(text = nombreMedico, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF002855))
                    Text(text = especialidad, fontSize = 14.sp, color = Color(0xFF6B7280))
                    Text(text = "CMP: $cmp", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Horarios Disponibles", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF002855))
            Spacer(modifier = Modifier.height(8.dp))


            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(horariosDisponibles) { horario ->
                    val isSelected = horario == horarioSeleccionado

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))

                            .background(if (isSelected) Color(0xFFE3001B) else Color(0xFFF3F4F6))
                            .clickable { onHorarioSelect(horario) }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = horario,
                            color = if (isSelected) Color.White else Color(0xFF002855),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}