package com.charahua.gestorcitassanna.presentation.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestorcitassanna.presentation.components.MinimalCard
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SannaBackground)
            .padding(top = 50.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Bienvenido", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = SannaPrimary)
                    Text(text = "Nombre", fontSize = 22.sp, fontWeight = FontWeight.Light, color = SannaPrimary)
                }
                Text(text = "SANNA Salud Control Central", fontSize = 14.sp, color = SannaSecondaryText)
            }

            TextButton(
                onClick = onLogout,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Salir", color = SannaError, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Column {
            Text(
                text = "Panel Administrativo",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SannaPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFEAEAF5)),
                        colors = CardDefaults.cardColors(containerColor = SannaSecondaryText)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = SannaSurface
                            ) {
                                Box(
                                    modifier = Modifier.size(50.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Business,
                                        contentDescription = "Sedes",
                                        tint = SannaPrimary,
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text("Sedes", fontSize = 14.sp, color = SannaSurface, fontWeight = FontWeight.Normal)
                                Text("${stats.sedesCount}", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = SannaSurface)
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFEAEAF5)),
                        colors = CardDefaults.cardColors(containerColor = SannaSecondaryText)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = SannaSurface
                            ) {
                                Box(
                                    modifier = Modifier.size(50.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Medicos",
                                        tint = SannaPrimary,
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text("Médicos", fontSize = 14.sp, color = SannaSurface)
                                Text("${stats.medicosCount}", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = SannaSurface)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFEAEAF5)),
                        colors = CardDefaults.cardColors(containerColor = SannaSecondaryText)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = SannaSurface
                            ) {
                                Box(
                                    modifier = Modifier.size(50.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = "Citas",
                                        tint = SannaPrimary,
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text("Citas", fontSize = 14.sp, color = SannaSurface)
                                Text("${stats.citasCount}", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = SannaSurface)
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFEAEAF5)),
                        colors = CardDefaults.cardColors(containerColor = SannaSecondaryText)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = SannaSurface
                            ) {
                                Box(
                                    modifier = Modifier.size(50.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Especialidades",
                                        tint = SannaPrimary,
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text("Especialidades", fontSize = 13.sp, color = SannaSurface)
                                Text("${stats.especialidadesCount}", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = SannaSurface)
                            }
                        }
                    }
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
