package com.charahua.gestorcitassanna.presentation.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestorcitassanna.data.local.entity.HorarioMedicoEntity
import com.charahua.gestorcitassanna.presentation.components.MinimalButton
import com.charahua.gestorcitassanna.presentation.components.MinimalCard
import com.charahua.gestorcitassanna.presentation.components.MinimalTextField
import com.charahua.gestorcitassanna.presentation.components.MinimalTopBar
import com.charahua.gestorcitassanna.presentation.viewmodel.AdminViewModel
import com.charahua.gestorcitassanna.ui.theme.*

@Composable
fun GestionHorariosScreen(
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val horarios by viewModel.horarios.collectAsState(initial = emptyList())
    var turnoSeleccionado by remember { mutableStateOf("Todos") }
    var medicoId by remember { mutableStateOf("1") }
    var sedeId by remember { mutableStateOf("1") }
    var dia by remember { mutableStateOf("LUNES") }
    var inicio by remember { mutableStateOf("08:00") }
    var fin by remember { mutableStateOf("14:00") }
    var showDialog by remember { mutableStateOf(false) }

    val horariosFiltrados = horarios.filter { horario ->
        val hora = horario.horaInicio

        when (turnoSeleccionado) {
            "Mañana" -> hora >= "06:00" && hora < "12:00"
            "Tarde" -> hora >= "12:00" && hora < "18:00"
            "Noche" -> hora >= "18:00" || hora < "06:00"
            else -> true
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(SannaBackground)) {

        MinimalTopBar(title = "Gestión de Horarios", onBackClick = onBack)
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp, start = 20.dp, top = 10.dp, bottom = 10.dp)) {


            MinimalButton(text = "+ Agregar Horario", onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Administración de turnos médicos",
                color = SannaSecondaryText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(all = 8.dp),

                )

            Row(
               modifier = Modifier.fillMaxWidth().padding(all = 15.dp),
               horizontalArrangement = Arrangement.Center,
               verticalAlignment = Alignment.CenterVertically

           ) {

               Spacer(modifier = Modifier.height(8.dp))
               Card(
                   modifier = Modifier.weight(1f),
                   shape = RoundedCornerShape(14.dp),
                   colors = CardDefaults.cardColors(containerColor = SannaBackground),
               )
               {
                   Column(
                       modifier = Modifier.padding(16.dp),
                       verticalArrangement = Arrangement.Center,
                       horizontalAlignment = Alignment.CenterHorizontally
                   ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = SannaSuccess,
                            modifier = Modifier.size(20.dp)
                        )
                       Spacer(modifier = Modifier.height(8.dp))

                       Text(
                           text = "10",
                           color = SannaPrimary,
                           fontSize = 20.sp,
                           fontWeight = FontWeight.Bold

                       )
                       Text(
                           text = "Horarios Registrados ",
                           color = SannaPrimary,
                           fontSize = 12.sp,
                           fontWeight = FontWeight.Normal
                       )



                   }
               }
               Card(
                   modifier = Modifier.weight(1f),
                   shape = RoundedCornerShape(14.dp),
                   colors = CardDefaults.cardColors(containerColor = SannaBackground),
               ) {
                   Column(
                       modifier = Modifier.padding(16.dp),
                       verticalArrangement = Arrangement.Center,
                       horizontalAlignment = Alignment.CenterHorizontally
                   ) {
                       Icon(
                           imageVector = Icons.Default.Watch,
                           contentDescription = null,
                           tint = SannaError,
                           modifier = Modifier.size(20.dp)
                       )
                       Spacer(modifier = Modifier.height(8.dp))

                       Text(
                           text = "8",
                           color = SannaPrimary,
                           fontSize = 20.sp,
                           fontWeight = FontWeight.Bold

                       )
                       Text(
                           text = "Atenciones del día ",
                           color = SannaPrimary,
                           fontSize = 12.sp,
                           fontWeight = FontWeight.Normal
                       )

                   }
               }
           }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Horarios Registrados",
                color = SannaPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold

            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Todos", "Mañana", "Tarde", "Noche").forEach { turno ->

                    FilterChip(
                        selected = turnoSeleccionado == turno,
                        onClick = { turnoSeleccionado = turno },
                        label = { Text(turno) }
                    )
                }
            }


            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                items(horariosFiltrados) { h ->
                    MinimalCard {
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("${h.diaSemana}: ${h.horaInicio} - ${h.horaFin}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SannaPrimary)
                                Text("Médico ID: ${h.medicoId} | Sede ID: ${h.sedeId}", fontSize = 13.sp, color = SannaSecondaryText)
                            }
                            IconButton(onClick = { viewModel.deleteHorario(h) }) { Text("X️", fontSize = 18.sp, color = SannaSecondaryText) }
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
