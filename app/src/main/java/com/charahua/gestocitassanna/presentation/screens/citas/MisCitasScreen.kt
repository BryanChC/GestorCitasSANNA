package com.charahua.gestocitassanna.presentation.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestocitassanna.presentation.components.EmptyState
import com.charahua.gestocitassanna.presentation.components.MinimalCard
import com.charahua.gestocitassanna.presentation.viewmodel.CitasViewModel
import com.charahua.gestocitassanna.ui.theme.*

@Composable
fun MisCitasScreen(
    viewModel: CitasViewModel,
    pacienteId: Long,
    onCitaClick: (Long) -> Unit
) {
    val citasFlow = remember(pacienteId) { viewModel.getCitasForPaciente(pacienteId) }
    val citas by citasFlow.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SannaBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Citas Médicas",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = SannaPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (citas.isEmpty()) {
            EmptyState(title = "No tienes citas programadas", subtitle = "Reserva tu primera cita con nuestros especialistas.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(citas) { cita ->
                    MinimalCard(
                        modifier = Modifier.clickable { onCitaClick(cita.id) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "${cita.fecha} - ${cita.hora}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SannaPrimary)
                                Surface(
                                    color = when (cita.estado) {
                                        "CONFIRMADA" -> SannaSuccess
                                        "CANCELADA" -> SannaError
                                        else -> SannaPrimary
                                    },
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(
                                        text = cita.estado,
                                        color = SannaOnPrimary,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Motivo: ${cita.motivo}", fontSize = 14.sp, color = SannaSecondaryText)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Costo: S/ ${cita.costo}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = SannaPrimary)
                        }
                    }
                }
            }
        }
    }
}
