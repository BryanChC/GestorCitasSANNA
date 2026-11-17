package com.charahua.gestocitassanna.presentation.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestocitassanna.presentation.components.MinimalCard
import com.charahua.gestocitassanna.presentation.viewmodel.AdminViewModel
import com.charahua.gestocitassanna.ui.theme.*

@Composable
fun HomeAdminScreen(
    viewModel: AdminViewModel,
    onNavigateToSedes: () -> Unit,
    onNavigateToEspecialidades: () -> Unit,
    onNavigateToMedicos: () -> Unit,
    onNavigateToHorarios: () -> Unit,
    onNavigateToCitas: () -> Unit,
    onNavigateToSeguros: () -> Unit,
    onNavigateToCostos: () -> Unit,
    onLogout: () -> Unit
) {
    val stats by viewModel.stats.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SannaBackground)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Panel de Administración", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = SannaPrimary)
                Text(text = "SANNA Salud Control Central", fontSize = 14.sp, color = SannaSecondaryText)
            }
            TextButton(onClick = onLogout) {
                Text("Salir", color = SannaError, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = SannaSecondary)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Sedes", fontSize = 12.sp, color = SannaSecondaryText)
                    Text("${stats.sedesCount}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SannaPrimary)
                }
            }
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = SannaSecondary)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Médicos", fontSize = 12.sp, color = SannaSecondaryText)
                    Text("${stats.medicosCount}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SannaPrimary)
                }
            }
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = SannaSecondary)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Citas", fontSize = 12.sp, color = SannaSecondaryText)
                    Text("${stats.citasCount}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SannaPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Módulos de Gestión", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SannaPrimary)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item { AdminMenuCard("Gestión de Sedes", "Administrar sedes y ubicaciones", onNavigateToSedes) }
            item { AdminMenuCard("Gestión de Especialidades", "Especialidades médicas y servicios", onNavigateToEspecialidades) }
            item { AdminMenuCard("Gestión de Médicos", "Staff médico y colegiaturas", onNavigateToMedicos) }
            item { AdminMenuCard("Gestión de Horarios", "Horarios y turnos de atención", onNavigateToHorarios) }
            item { AdminMenuCard("Gestión de Citas", "Monitoreo y estados de citas", onNavigateToCitas) }
            item { AdminMenuCard("Gestión de Seguros", "Aseguradoras y convenios de cobertura", onNavigateToSeguros) }
            item { AdminMenuCard("Gestión de Costos (9 Módulos)", "Insumos, materiales, salarios y gastos operativos", onNavigateToCostos) }
        }
    }
}

@Composable
fun AdminMenuCard(title: String, subtitle: String, onClick: () -> Unit) {
    MinimalCard(modifier = Modifier.clickable { onClick() }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SannaPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, fontSize = 13.sp, color = SannaSecondaryText)
        }
    }
}
