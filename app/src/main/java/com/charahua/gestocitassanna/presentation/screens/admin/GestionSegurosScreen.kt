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
import com.charahua.gestocitassanna.data.local.entity.AseguradoraEntity
import com.charahua.gestocitassanna.presentation.components.MinimalButton
import com.charahua.gestocitassanna.presentation.components.MinimalCard
import com.charahua.gestocitassanna.presentation.components.MinimalTextField
import com.charahua.gestocitassanna.presentation.components.MinimalTopBar
import com.charahua.gestocitassanna.presentation.viewmodel.AdminViewModel
import com.charahua.gestocitassanna.ui.theme.*

@Composable
fun GestionSegurosScreen(
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val aseguradoras by viewModel.aseguradoras.collectAsState(initial = emptyList())
    var nombre by remember { mutableStateOf("") }
    var ruc by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(SannaBackground)) {
        MinimalTopBar(title = "Gestión de Aseguradoras y Convenios", onBackClick = onBack)
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            MinimalButton(text = "+ Agregar Aseguradora", onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(aseguradoras) { aseg ->
                    MinimalCard {
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(aseg.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SannaPrimary)
                                Text("RUC: ${aseg.ruc} | Contacto: ${aseg.contacto}", fontSize = 13.sp, color = SannaSecondaryText)
                            }
                            IconButton(onClick = { viewModel.deleteAseguradora(aseg) }) { Text("🗑️", fontSize = 18.sp) }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Nueva Aseguradora", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    MinimalTextField(value = nombre, onValueChange = { nombre = it }, label = "Nombre Aseguradora")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = ruc, onValueChange = { ruc = it }, label = "RUC")
                    Spacer(modifier = Modifier.height(8.dp))
                    MinimalTextField(value = contacto, onValueChange = { contacto = it }, label = "Teléfono de Contacto")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (nombre.isNotBlank()) {
                        viewModel.addAseguradora(AseguradoraEntity(nombre = nombre, ruc = ruc, contacto = contacto))
                        nombre = ""; ruc = ""; contacto = ""
                        showDialog = false
                    }
                }) { Text("Guardar", color = SannaPrimary, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancelar", color = SannaSecondaryText) } },
            containerColor = SannaBackground
        )
    }
}
