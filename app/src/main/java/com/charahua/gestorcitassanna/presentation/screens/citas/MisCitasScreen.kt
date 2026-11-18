package com.charahua.gestorcitassanna.presentation.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestorcitassanna.presentation.components.CitaCategoriaCard
import com.charahua.gestorcitassanna.presentation.components.EmptyState
import com.charahua.gestorcitassanna.presentation.components.MinimalCard
import com.charahua.gestorcitassanna.presentation.viewmodel.CitasViewModel
import com.charahua.gestorcitassanna.ui.theme.*

@Composable
fun MisCitasScreen(
    viewModel: CitasViewModel,
    pacienteId: Long,
    onCitaClick: (Long) -> Unit
) {

    val citasFlow = remember(pacienteId) {
        viewModel.getCitasForPaciente(pacienteId)
    }

    val citas by citasFlow.collectAsState(
        initial = emptyList()
    )

    var filtro by remember {
        mutableStateOf("PROGRAMADAS")
    }
    val citasFiltradas = when (filtro) {

        "PROGRAMADAS" -> citas.filter {
            it.estado == "PROGRAMADA" ||
                    it.estado == "CONFIRMADA" ||
                    it.estado == "REPROGRAMADA"
        }

        "REALIZADAS" -> citas.filter {
            it.estado == "ATENDIDA"
        }

        "CANCELADAS" -> citas.filter {
            it.estado == "CANCELADA"
        }

        else -> citas
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SannaBackground)
            .padding(16.dp)
    ) {

        Text(
            text = "Mis Citas Médicas",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = SannaPrimary
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        CitaCategoriaCard(
            titulo = "Citas Programadas",
            descripcion = "${citas.count {
                it.estado == "PROGRAMADA" ||
                        it.estado == "CONFIRMADA" ||
                        it.estado == "REPROGRAMADA"
            }} citas",
            icono = Icons.Default.DateRange,
            onClick = {
                filtro = "PROGRAMADAS"
            }
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        CitaCategoriaCard(
            titulo = "Atenciones Realizadas",
            descripcion = "${citas.count {
                it.estado == "ATENDIDA"
            }} citas",
            icono = Icons.Default.MedicalServices,
            onClick = {
                filtro = "REALIZADAS"
            }
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        CitaCategoriaCard(
            titulo = "Citas Canceladas",
            descripcion = "${citas.count {
                it.estado == "CANCELADA"
            }} citas",
            icono = Icons.Default.Schedule,
            onClick = {
                filtro = "CANCELADAS"
            }
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )
        Text(
            text = "Categoría seleccionada: $filtro",
            fontSize = 13.sp,
            color = SannaSecondaryText,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Citas encontradas: ${citasFiltradas.size}",
            color = SannaPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        if (citasFiltradas.isEmpty()) {

            EmptyState(
                title = "No hay citas en esta categoría",
                subtitle = "Seleccione otra categoría o registre una nueva cita."
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "DEBUG: ${citasFiltradas.size} citas",
                        color = SannaPrimary
                    )
                }
                items(citasFiltradas) { cita ->

                    MinimalCard(
                        modifier = Modifier.clickable {
                            onCitaClick(cita.id)
                        }
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = "${cita.fecha} - ${cita.hora}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = SannaPrimary
                                )

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
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                    )

                                }

                            }

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text = "Motivo: ${cita.motivo}",
                                fontSize = 14.sp,
                                color = SannaSecondaryText
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Costo: S/ ${cita.costo}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SannaPrimary
                            )

                        }

                    }

                }

            }

        }

    }

}