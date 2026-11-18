package com.charahua.gestorcitassanna.presentation.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestorcitassanna.presentation.components.MinimalCard
import com.charahua.gestorcitassanna.presentation.components.TarjetaMetricaSanna
import com.charahua.gestorcitassanna.presentation.viewmodel.AdminViewModel
import com.charahua.gestorcitassanna.ui.theme.*

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


    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SannaBackground)
            .systemBarsPadding() // Soluciona el problema del texto chocando arriba
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .verticalScroll(scrollState), // Agrega el scroll a toda la columna junta
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Panel de Administración", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = SannaPrimary)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "SANNA Salud Control Central", fontSize = 14.sp, color = SannaSecondaryText)
            }
            TextButton(onClick = onLogout) {
                Text("Salir", color = SannaError, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(color = SannaSecondary, thickness = 1.dp)

        Text(text = "Resumen Operativo", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SannaPrimary)


        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaMetricaSanna(
                    titulo = "Sedes",
                    valor = "${stats.sedesCount}",
                    icono = Icons.Filled.Domain,
                    colorIcono = SannaPrimary,
                    modifier = Modifier.weight(1f)
                )
                TarjetaMetricaSanna(
                    titulo = "Especialidades",
                    valor = "12",
                    icono = Icons.Filled.MedicalServices,
                    colorIcono = SannaError,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaMetricaSanna(
                    titulo = "Médicos",
                    valor = "${stats.medicosCount}",
                    icono = Icons.Filled.PeopleAlt,
                    colorIcono = SannaPrimary,
                    modifier = Modifier.weight(1f)
                )
                TarjetaMetricaSanna(
                    titulo = "Citas",
                    valor = "${stats.citasCount}",
                    icono = Icons.Filled.EventAvailable,
                    colorIcono = SannaError,
                    modifier = Modifier.weight(1f)
                )
            }
        }


        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Módulos de Gestión", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SannaPrimary)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AdminMenuCard("Gestión de Sedes", "Administrar sedes y ubicaciones", onNavigateToSedes)
            AdminMenuCard("Gestión de Especialidades", "Especialidades médicas y servicios", onNavigateToEspecialidades)
            AdminMenuCard("Gestión de Médicos", "Staff médico y colegiaturas", onNavigateToMedicos)
            AdminMenuCard("Gestión de Horarios", "Horarios y turnos de atención", onNavigateToHorarios)
            AdminMenuCard("Gestión de Citas", "Monitoreo y estados de citas", onNavigateToCitas)
            AdminMenuCard("Gestión de Seguros", "Aseguradoras y convenios de cobertura", onNavigateToSeguros)
            AdminMenuCard("Gestión de Costos (9 Módulos)", "Insumos, materiales, salarios y gastos operativos", onNavigateToCostos)

            // Añadimos un espacio extra al final para que el último botón no quede pegado al borde inferior al deslizar
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun AdminMenuCard(title: String, subtitle: String, onClick: () -> Unit) {
    MinimalCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SannaPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, fontSize = 13.sp, color = SannaSecondaryText)
        }
    }
}