package com.charahua.gestorcitassanna.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.charahua.gestorcitassanna.presentation.screens.citas.BuscarMedicoScreen
import com.charahua.gestorcitassanna.presentation.screens.citas.MisCitasScreen
import com.charahua.gestorcitassanna.presentation.screens.perfil.PerfilScreen
import com.charahua.gestorcitassanna.presentation.viewmodel.CitasViewModel
import com.charahua.gestorcitassanna.ui.theme.*

@Composable
fun HomePacienteScreen(
    citasViewModel: CitasViewModel,
    pacienteId: Long,
    userId: Long,
    onNavigateToReservar: () -> Unit,
    onCitaClick: (Long) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = SannaBackground,
                tonalElevation = 6.dp
            ) {

                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Mis Citas",
                            tint = if (selectedTab == 0) SannaPrimary else SannaSecondaryText
                        )
                    },
                    label = { Text("Mis Citas", color = if (selectedTab == 0) SannaPrimary else SannaSecondaryText) }
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = "Busca tu médico",
                            tint = if (selectedTab == 1) SannaPrimary else SannaSecondaryText
                        )
                    },
                    label = {
                        Text(
                            text = "Busca tu médico",
                            color = if (selectedTab == 1) SannaPrimary else SannaSecondaryText,
                            maxLines = 1 // Para evitar que se desborde el texto
                        )
                    }
                )


                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigateToReservar() },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = "Reservar Cita",
                            tint = SannaSecondaryText
                        )
                    },
                    label = { Text("Reservar", color = SannaSecondaryText) }
                )


                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = if (selectedTab == 3) SannaPrimary else SannaSecondaryText
                        )
                    },
                    label = { Text("Perfil", color = if (selectedTab == 3) SannaPrimary else SannaSecondaryText) }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().background(SannaBackground)) {
            when (selectedTab) {
                0 -> MisCitasScreen(viewModel = citasViewModel, pacienteId = pacienteId, onCitaClick = onCitaClick)
                1 -> BuscarMedicoScreen(viewModel = citasViewModel, pacienteId = pacienteId, onBackToReservar = { selectedTab = 0 })
                3 -> PerfilScreen(userId = userId, onLogout = onLogout)
            }
        }
    }
}