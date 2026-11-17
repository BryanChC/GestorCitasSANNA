package com.charahua.gestorcitassanna.presentation.screens.admin

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
import com.charahua.gestorcitassanna.data.local.entity.SedeEntity
import com.charahua.gestorcitassanna.presentation.components.MinimalButton
import com.charahua.gestorcitassanna.presentation.components.MinimalCard
import com.charahua.gestorcitassanna.presentation.components.MinimalTextField
import com.charahua.gestorcitassanna.presentation.components.MinimalTopBar
import com.charahua.gestorcitassanna.presentation.viewmodel.AdminViewModel
import com.charahua.gestorcitassanna.ui.theme.*

@Composable
fun GestionSedesScreen(
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val sedes by viewModel.sedes.collectAsState(initial = emptyList())

    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var distrito by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SannaBackground)
    ) {
        MinimalTopBar(title = "Gestión de Sedes", onBackClick = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            MinimalButton(
                text = "+ Agregar Nueva Sede",
                onClick = { showAddDialog = true },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(sedes) { sede ->
                    MinimalCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(sede.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SannaPrimary)
                                Text("${sede.direccion} - ${sede.distrito}", fontSize = 13.sp, color = SannaSecondaryText)
                                Text("Tel: ${sede.telefono}", fontSize = 13.sp, color = SannaSecondaryText)
                            }
                            IconButton(onClick = { viewModel.deleteSede(sede) }) {
                                Text("🗑️", fontSize = 18.sp)
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
            title = { Text("Nueva Sede", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    MinimalTextField(value = nombre, onValueChange = { nombre = it }, label = "Nombre Sede")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = direccion, onValueChange = { direccion = it }, label = "Dirección")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = distrito, onValueChange = { distrito = it }, label = "Distrito")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = telefono, onValueChange = { telefono = it }, label = "Teléfono")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (nombre.isNotBlank()) {
                        viewModel.addSede(SedeEntity(nombre = nombre, direccion = direccion, distrito = distrito, telefono = telefono))
                        nombre = ""
                        direccion = ""
                        distrito = ""
                        telefono = ""
                        showAddDialog = false
                    }
                }) {
                    Text("Guardar", color = SannaPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancelar", color = SannaSecondaryText)
                }
            },
            containerColor = SannaBackground
        )
    }
}
