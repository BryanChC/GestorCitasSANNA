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
import com.charahua.gestocitassanna.data.local.entity.InsumoEntity
import com.charahua.gestocitassanna.presentation.components.MinimalButton
import com.charahua.gestocitassanna.presentation.components.MinimalCard
import com.charahua.gestocitassanna.presentation.components.MinimalTextField
import com.charahua.gestocitassanna.presentation.components.MinimalTopBar
import com.charahua.gestocitassanna.presentation.viewmodel.CostosViewModel
import com.charahua.gestocitassanna.ui.theme.*

@Composable
fun GestionCostosScreen(
    costosViewModel: CostosViewModel,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val insumos by costosViewModel.insumos.collectAsState()
    val materiales by costosViewModel.materiales.collectAsState()
    val tiempoProductivo by costosViewModel.tiempoProductivo.collectAsState()
    val costoSalarial by costosViewModel.costoSalarial.collectAsState()
    val gastosOperativos by costosViewModel.gastosOperativos.collectAsState()
    val sueldosAdmin by costosViewModel.sueldosAdmin.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var nombreInsumo by remember { mutableStateOf("") }
    var costoInsumo by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(SannaBackground)) {
        MinimalTopBar(title = "Módulos de Costos (9 Módulos CSV)", onBackClick = onBack)

        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = SannaSecondary,
            contentColor = SannaPrimary
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Insumos") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Materiales") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Tiempo Prod.") })
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Salarios Dir.") })
            Tab(selected = selectedTab == 4, onClick = { selectedTab = 4 }, text = { Text("Gastos Op.") })
            Tab(selected = selectedTab == 5, onClick = { selectedTab = 5 }, text = { Text("Sueldos Admin") })
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            MinimalButton(text = "+ Agregar Registro", onClick = { showAddDialog = true }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                when (selectedTab) {
                    0 -> items(insumos) { item ->
                        MinimalCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(item.nombre, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SannaPrimary)
                                Text("Cat: ${item.categoria} | Stock: ${item.stock}", fontSize = 13.sp, color = SannaSecondaryText)
                                Text("Costo Unitario: S/ ${item.costoUnitario}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SannaPrimary)
                            }
                        }
                    }
                    1 -> items(materiales) { item ->
                        MinimalCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(item.nombre, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SannaPrimary)
                                Text("Tipo: ${item.tipo} | Vida Util: ${item.vidaUtilMeses} meses", fontSize = 13.sp, color = SannaSecondaryText)
                                Text("Costo: S/ ${item.costo}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SannaPrimary)
                            }
                        }
                    }
                    2 -> items(tiempoProductivo) { item ->
                        MinimalCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(item.descripcion, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SannaPrimary)
                                Text("Horas Mensuales: ${item.horasMensuales}", fontSize = 13.sp, color = SannaSecondaryText)
                                Text("Costo Hora: S/ ${item.costoHora}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SannaPrimary)
                            }
                        }
                    }
                    3 -> items(costoSalarial) { item ->
                        MinimalCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(item.cargo, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SannaPrimary)
                                Text("Sueldo Base: S/ ${item.sueldoBase} | Beneficios: S/ ${item.beneficiosSociales}", fontSize = 13.sp, color = SannaSecondaryText)
                                Text("Total: S/ ${item.total}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SannaPrimary)
                            }
                        }
                    }
                    4 -> items(gastosOperativos) { item ->
                        MinimalCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(item.concepto, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SannaPrimary)
                                Text("Tipo: ${item.tipoGasto}", fontSize = 13.sp, color = SannaSecondaryText)
                                Text("Monto Mensual: S/ ${item.montoMensual}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SannaPrimary)
                            }
                        }
                    }
                    5 -> items(sueldosAdmin) { item ->
                        MinimalCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(item.puesto, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SannaPrimary)
                                Text("Depto: ${item.departamento}", fontSize = 13.sp, color = SannaSecondaryText)
                                Text("Sueldo: S/ ${item.sueldo}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SannaPrimary)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Agregar Nuevo Registro", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    MinimalTextField(value = nombreInsumo, onValueChange = { nombreInsumo = it }, label = "Nombre / Concepto")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = costoInsumo, onValueChange = { costoInsumo = it }, label = "Costo / Monto (S/)")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (nombreInsumo.isNotBlank()) {
                        val costo = costoInsumo.toDoubleOrNull() ?: 10.0
                        if (selectedTab == 0) {
                            costosViewModel.addInsumo(InsumoEntity(nombre = nombreInsumo, categoria = "General", costoUnitario = costo, stock = 100))
                        }
                        nombreInsumo = ""
                        costoInsumo = ""
                        showAddDialog = false
                    }
                }) { Text("Guardar", color = SannaPrimary, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { showAddDialog = false }) { Text("Cancelar", color = SannaSecondaryText) } },
            containerColor = SannaBackground
        )
    }
}
