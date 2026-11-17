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
import com.charahua.gestocitassanna.presentation.components.MinimalCard
import com.charahua.gestocitassanna.presentation.components.MinimalTopBar
import com.charahua.gestocitassanna.presentation.viewmodel.AdminViewModel
import com.charahua.gestocitassanna.ui.theme.*

@Composable
fun GestionCitasScreen(
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val citas by viewModel.citas.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().background(SannaBackground)) {
        MinimalTopBar(title = "Gestión General de Citas", onBackClick = onBack)
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(citas) { cita ->
                    MinimalCard {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Cita #${cita.id} - ${cita.fecha} ${cita.hora}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SannaPrimary)
                                Surface(color = SannaPrimary, shape = MaterialTheme.shapes.small) {
                                    Text(cita.estado, color = SannaOnPrimary, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Motivo: ${cita.motivo}", fontSize = 13.sp, color = SannaSecondaryText)
                            Text("Costo: S/ ${cita.costo}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SannaPrimary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { viewModel.updateCitaEstado(cita.id, "CONFIRMADA") }, colors = ButtonDefaults.buttonColors(containerColor = SannaSuccess)) {
                                    Text("Confirmar", fontSize = 12.sp)
                                }
                                Button(onClick = { viewModel.updateCitaEstado(cita.id, "ATENDIDA") }, colors = ButtonDefaults.buttonColors(containerColor = SannaPrimary)) {
                                    Text("Atendida", fontSize = 12.sp)
                                }
                                OutlinedButton(onClick = { viewModel.updateCitaEstado(cita.id, "CANCELADA") }) {
                                    Text("Cancelar", fontSize = 12.sp, color = SannaError)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
