package com.charahua.gestocitassanna.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.charahua.gestocitassanna.presentation.screens.citas.MisCitasScreen
import com.charahua.gestocitassanna.presentation.screens.perfil.PerfilScreen
import com.charahua.gestocitassanna.presentation.viewmodel.CitasViewModel
import com.charahua.gestocitassanna.ui.theme.*

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
                    onClick = { onNavigateToReservar() },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = "Reservar Cita",
                            tint = SannaPrimary
                        )
                    },
                    label = { Text("Reservar", color = SannaPrimary) }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = if (selectedTab == 2) SannaPrimary else SannaSecondaryText
                        )
                    },
                    label = { Text("Perfil", color = if (selectedTab == 2) SannaPrimary else SannaSecondaryText) }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().background(SannaBackground)) {
            when (selectedTab) {
                0 -> MisCitasScreen(viewModel = citasViewModel, pacienteId = pacienteId, onCitaClick = onCitaClick)
                2 -> PerfilScreen(userId = userId, onLogout = onLogout)
            }
        }
    }
}
