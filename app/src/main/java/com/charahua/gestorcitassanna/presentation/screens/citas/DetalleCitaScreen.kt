package com.charahua.gestorcitassanna.presentation.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestorcitassanna.data.local.entity.CitaEntity
import com.charahua.gestorcitassanna.presentation.components.ConfirmationDialog
import com.charahua.gestorcitassanna.presentation.components.MinimalButton
import com.charahua.gestorcitassanna.presentation.components.MinimalTopBar
import com.charahua.gestorcitassanna.presentation.viewmodel.CitasViewModel
import com.charahua.gestorcitassanna.ui.theme.*

@Composable
fun DetalleCitaScreen(
    viewModel: CitasViewModel,
    citaId: Long,
    onBack: () -> Unit
) {
    var cita by remember { mutableStateOf<CitaEntity?>(null) }
    var showReprogramarDialog by remember { mutableStateOf(false) }
    var showCancelarDialog by remember { mutableStateOf(false) }

    LaunchedEffect(citaId) {
        cita = viewModel.getCitaById(citaId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SannaBackground)
    ) {
        MinimalTopBar(title = "Detalle de Cita", onBackClick = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text("Gestión y Detalles de Cita #$citaId", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SannaPrimary)
            Spacer(
                modifier = Modifier.height(20.dp)
            )

            cita?.let {

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "Estado: ${it.estado}",
                            fontWeight = FontWeight.Bold,
                            color = SannaPrimary
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text("Fecha: ${it.fecha}")

                        Text("Hora: ${it.hora}")

                        Text("Motivo: ${it.motivo}")

                        Text("Costo: S/ ${it.costo}")

                    }

                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

            }
            Spacer(modifier = Modifier.height(24.dp))
            MinimalButton(
                text = "Reprogramar Cita",
                onClick = { showReprogramarDialog = true },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            MinimalButton(
                text = "Cancelar Cita",
                onClick = { showCancelarDialog = true },
                modifier = Modifier.fillMaxWidth(),
                isPrimary = false
            )
        }
    }

    if (showCancelarDialog) {
        ConfirmationDialog(
            title = "Cancelar Cita",
            message = "¿Está seguro de que desea cancelar esta cita médica?",
            onConfirm = {
                viewModel.cancelarCita(citaId)
                showCancelarDialog = false
                onBack()
            },
            onDismiss = { showCancelarDialog = false }
        )
    }

    if (showReprogramarDialog) {
        ReprogramarCitaDialog(
            citaId = citaId,
            viewModel = viewModel,
            onDismiss = { showReprogramarDialog = false },
            onSuccess = {
                showReprogramarDialog = false
                onBack()
            }
        )
    }
}

@Composable
fun ReprogramarCitaDialog(
    citaId: Long,
    viewModel: CitasViewModel,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var nuevaFecha by remember { mutableStateOf("2025-03-25") }
    var nuevaHora by remember { mutableStateOf("10:00") }
    var motivo by remember { mutableStateOf("Cambio de horario personal") }

    val result by viewModel.reprogramarResult.collectAsState()

    LaunchedEffect(result) {
        if (result != null && result!!.isSuccess) {
            onSuccess()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reprogramar Cita", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(value = nuevaFecha, onValueChange = { nuevaFecha = it }, label = { Text("Nueva Fecha (YYYY-MM-DD)") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = nuevaHora, onValueChange = { nuevaHora = it }, label = { Text("Nueva Hora (HH:MM)") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = motivo, onValueChange = { motivo = it }, label = { Text("Motivo de reprogramación") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.reprogramarCita(citaId, nuevaFecha, nuevaHora, motivo)
            }) {
                Text("Reprogramar", color = SannaPrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = SannaSecondaryText)
            }
        },
        containerColor = SannaBackground
    )
}
