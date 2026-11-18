package com.charahua.gestorcitassanna.presentation.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestorcitassanna.data.local.entity.HorarioMedicoEntity
import com.charahua.gestorcitassanna.presentation.components.MinimalButton
import com.charahua.gestorcitassanna.presentation.components.MinimalCard
import com.charahua.gestorcitassanna.presentation.components.MinimalTopBar
import com.charahua.gestorcitassanna.presentation.viewmodel.AdminViewModel
import com.charahua.gestorcitassanna.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionHorariosScreen(
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val horarios by viewModel.horarios.collectAsState(initial = emptyList())

    // Variables de estado para el nuevo formulario
    var showDialog by remember { mutableStateOf(false) }
    var medicoId by remember { mutableStateOf("1") }
    var sedeId by remember { mutableStateOf("1") }
    var diaSeleccionado by remember { mutableStateOf("LUNES") }
    var turnoSeleccionado by remember { mutableStateOf("Mañana (08:00 - 14:00)") }

    // Opciones para los menús desplegables
    val dias = listOf("LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO")
    val turnos = listOf("Mañana (08:00 - 14:00)", "Tarde (14:00 - 20:00)", "Noche (20:00 - 08:00)")

    Column(modifier = Modifier.fillMaxSize().background(SannaBackground)) {
        MinimalTopBar(title = "Gestión de Horarios Médicos", onBackClick = onBack)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Turnos Activos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SannaPrimary)
                    Text("Programación semanal de la sede", fontSize = 13.sp, color = SannaSecondaryText)
                }

                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = SannaPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar Turno")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (horarios.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay horarios programados actualmente.", color = SannaSecondaryText)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(horarios) { h ->
                        MinimalCard {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Icon(
                                        imageVector = Icons.Filled.CalendarMonth,
                                        contentDescription = null,
                                        tint = SannaPrimary,
                                        modifier = Modifier.size(28.dp).padding(end = 8.dp)
                                    )
                                    Column {
                                        Text("${h.diaSemana} | ${h.horaInicio} - ${h.horaFin}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SannaPrimary)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("Médico ID: ${h.medicoId} • Sede ID: ${h.sedeId}", fontSize = 13.sp, color = SannaSecondaryText)
                                    }
                                }
                                IconButton(onClick = { viewModel.deleteHorario(h) }) {
                                    Icon(imageVector = Icons.Filled.DeleteOutline, contentDescription = "Eliminar", tint = SannaError)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Programar Nuevo Turno", fontWeight = FontWeight.Bold, color = SannaPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    // Selector de Médico y Sede (Mantenemos la lógica de Bryan pero más limpia)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = medicoId, onValueChange = { medicoId = it },
                            label = { Text("ID Médico", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = sedeId, onValueChange = { sedeId = it },
                            label = { Text("ID Sede", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    // Menú Desplegable para el Día
                    Column {
                        var expandedDia by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(expanded = expandedDia, onExpandedChange = { expandedDia = it }) {
                            OutlinedTextField(
                                value = diaSeleccionado, onValueChange = {}, readOnly = true,
                                label = { Text("Día de la Semana") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDia) },
                                modifier = Modifier.fillMaxWidth().menuAnchor(),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SannaPrimary)
                            )
                            ExposedDropdownMenu(expanded = expandedDia, onDismissRequest = { expandedDia = false }) {
                                dias.forEach { selectionOption ->
                                    DropdownMenuItem(text = { Text(selectionOption) }, onClick = { diaSeleccionado = selectionOption; expandedDia = false })
                                }
                            }
                        }
                    }

                    // Menú Desplegable para el Turno (Calcula automáticamente Inicio y Fin)
                    Column {
                        var expandedTurno by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(expanded = expandedTurno, onExpandedChange = { expandedTurno = it }) {
                            OutlinedTextField(
                                value = turnoSeleccionado, onValueChange = {}, readOnly = true,
                                label = { Text("Horario") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTurno) },
                                modifier = Modifier.fillMaxWidth().menuAnchor(),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SannaPrimary)
                            )
                            ExposedDropdownMenu(expanded = expandedTurno, onDismissRequest = { expandedTurno = false }) {
                                turnos.forEach { selectionOption ->
                                    DropdownMenuItem(text = { Text(selectionOption) }, onClick = { turnoSeleccionado = selectionOption; expandedTurno = false })
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                MinimalButton(
                    text = "Guardar",
                    onClick = {
                        val mId = medicoId.toLongOrNull() ?: 1L
                        val sId = sedeId.toLongOrNull() ?: 1L


                        val horaInicio = turnoSeleccionado.substringAfter("(").substringBefore(" -").trim()
                        val horaFin = turnoSeleccionado.substringAfter("- ").substringBefore(")").trim()

                        viewModel.addHorario(
                            HorarioMedicoEntity(
                                medicoId = mId,
                                sedeId = sId,
                                diaSemana = diaSeleccionado,
                                horaInicio = horaInicio,
                                horaFin = horaFin
                            )
                        )
                        showDialog = false
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancelar", color = SannaSecondaryText) }
            },
            containerColor = SannaBackground
        )
    }
}