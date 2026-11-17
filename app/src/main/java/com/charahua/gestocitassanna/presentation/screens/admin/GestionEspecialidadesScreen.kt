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
import com.charahua.gestocitassanna.data.local.entity.EspecialidadEntity
import com.charahua.gestocitassanna.presentation.components.MinimalButton
import com.charahua.gestocitassanna.presentation.components.MinimalCard
import com.charahua.gestocitassanna.presentation.components.MinimalTextField
import com.charahua.gestocitassanna.presentation.components.MinimalTopBar
import com.charahua.gestocitassanna.presentation.viewmodel.AdminViewModel
import com.charahua.gestocitassanna.ui.theme.*

@Composable
fun GestionEspecialidadesScreen(
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val especialidades by viewModel.especialidades.collectAsState(initial = emptyList())
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SannaBackground)
    ) {
        MinimalTopBar(title = "Gestión de Especialidades", onBackClick = onBack)
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            MinimalButton(text = "+ Agregar Especialidad", onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(especialidades) { esp ->
                    MinimalCard {
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(esp.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SannaPrimary)
                                Text(esp.descripcion, fontSize = 13.sp, color = SannaSecondaryText)
                            }
                            IconButton(onClick = { viewModel.deleteEspecialidad(esp) }) {
                                Text("🗑️", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Nueva Especialidad", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    MinimalTextField(value = nombre, onValueChange = { nombre = it }, label = "Nombre")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = descripcion, onValueChange = { descripcion = it }, label = "Descripción")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (nombre.isNotBlank()) {
                        viewModel.addEspecialidad(EspecialidadEntity(nombre = nombre, descripcion = descripcion))
                        nombre = ""
                        descripcion = ""
                        showDialog = false
                    }
                }) { Text("Guardar", color = SannaPrimary, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancelar", color = SannaSecondaryText) } },
            containerColor = SannaBackground
        )
    }
}
