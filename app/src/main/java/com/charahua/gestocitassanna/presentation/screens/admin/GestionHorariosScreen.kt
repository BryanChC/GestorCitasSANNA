package com.charahua.gestocitassanna.presentation.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestocitassanna.data.local.entity.HorarioMedicoEntity
import com.charahua.gestocitassanna.presentation.components.MinimalButton
import com.charahua.gestocitassanna.presentation.components.MinimalCard
import com.charahua.gestocitassanna.presentation.components.MinimalTextField
import com.charahua.gestocitassanna.presentation.components.MinimalTopBar
import com.charahua.gestocitassanna.presentation.viewmodel.AdminViewModel
import com.charahua.gestocitassanna.ui.theme.*

@Composable
fun GestionHorariosScreen(
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val horarios by viewModel.horarios.collectAsState(initial = emptyList())
    var medicoId by remember { mutableStateOf("1") }
    var sedeId by remember { mutableStateOf("1") }
    var dia by remember { mutableStateOf("LUNES") }
    var inicio by remember { mutableStateOf("08:00") }
    var fin by remember { mutableStateOf("14:00") }
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(SannaBackground)) {
        MinimalTopBar(title = "Gestión de Horarios", onBackClick = onBack)
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            MinimalButton(text = "+ Agregar Horario", onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(horarios) { h ->
                    MinimalCard {
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("${h.diaSemana}: ${h.horaInicio} - ${h.horaFin}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SannaPrimary)
                                Text("Médico ID: ${h.medicoId} | Sede ID: ${h.sedeId}", fontSize = 13.sp, color = SannaSecondaryText)
                            }
                            IconButton(onClick = { viewModel.deleteHorario(h) }) { Text("🗑️", fontSize = 18.sp) }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Nuevo Horario", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    MinimalTextField(value = medicoId, onValueChange = { medicoId = it }, label = "ID Médico")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = sedeId, onValueChange = { sedeId = it }, label = "ID Sede")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = dia, onValueChange = { dia = it }, label = "Día (e.g. LUNES)")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = inicio, onValueChange = { inicio = it }, label = "Hora Inicio (HH:MM)")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = fin, onValueChange = { fin = it }, label = "Hora Fin (HH:MM)")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val mId = medicoId.toLongOrNull() ?: 1L
                    val sId = sedeId.toLongOrNull() ?: 1L
                    viewModel.addHorario(HorarioMedicoEntity(medicoId = mId, sedeId = sId, diaSemana = dia, horaInicio = inicio, horaFin = fin))
                    showDialog = false
                }) { Text("Guardar", color = SannaPrimary, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancelar", color = SannaSecondaryText) } },
            containerColor = SannaBackground
        )
    }
}
