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
import com.charahua.gestorcitassanna.data.local.entity.MedicoEntity
import com.charahua.gestorcitassanna.presentation.components.MinimalButton
import com.charahua.gestorcitassanna.presentation.components.MinimalCard
import com.charahua.gestorcitassanna.presentation.components.MinimalTextField
import com.charahua.gestorcitassanna.presentation.components.MinimalTopBar
import com.charahua.gestorcitassanna.presentation.viewmodel.AdminViewModel
import com.charahua.gestorcitassanna.ui.theme.*

@Composable
fun GestionMedicosScreen(
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val medicos by viewModel.medicos.collectAsState(initial = emptyList())
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var cmp by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(SannaBackground)) {
        MinimalTopBar(title = "Gestión de Médicos", onBackClick = onBack)
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            MinimalButton(text = "+ Agregar Médico", onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(medicos) { med ->
                    MinimalCard {
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Dr. ${med.nombre} ${med.apellido}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SannaPrimary)
                                Text("CMP: ${med.colegiatura} | Tel: ${med.telefono}", fontSize = 13.sp, color = SannaSecondaryText)
                                Text(med.email, fontSize = 13.sp, color = SannaSecondaryText)
                            }
                            IconButton(onClick = { viewModel.deleteMedico(med) }) { Text("🗑️", fontSize = 18.sp) }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Nuevo Médico", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    MinimalTextField(value = nombre, onValueChange = { nombre = it }, label = "Nombre")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = apellido, onValueChange = { apellido = it }, label = "Apellido")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = cmp, onValueChange = { cmp = it }, label = "Colegiatura (CMP)")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = telefono, onValueChange = { telefono = it }, label = "Teléfono")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = email, onValueChange = { email = it }, label = "Correo")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (nombre.isNotBlank() && cmp.isNotBlank()) {
                        viewModel.addMedico(MedicoEntity(nombre = nombre, apellido = apellido, colegiatura = cmp, telefono = telefono, email = email))
                        nombre = ""; apellido = ""; cmp = ""; telefono = ""; email = ""
                        showDialog = false
                    }
                }) { Text("Guardar", color = SannaPrimary, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancelar", color = SannaSecondaryText) } },
            containerColor = SannaBackground
        )
    }
}
