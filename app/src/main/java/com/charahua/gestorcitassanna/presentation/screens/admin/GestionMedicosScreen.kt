package com.charahua.gestorcitassanna.presentation.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestorcitassanna.data.local.entity.MedicoEntity
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
    var searchText by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var cmp by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val filteredMedicos = remember(medicos, searchText) {
        if (searchText.isBlank()) medicos
        else medicos.filter {
            it.nombre.contains(searchText, ignoreCase = true) ||
            it.apellido.contains(searchText, ignoreCase = true) ||
            it.colegiatura.contains(searchText, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SannaBackground)
    ) {
        MinimalTopBar(
            title = "Gestión de Médicos",
            onBackClick = onBack
        )

        // Barra de Búsqueda con botón de agregar a su lado derecho
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Buscar médico...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SannaPrimary,
                    unfocusedBorderColor = SannaBorder,
                    focusedLabelColor = SannaPrimary,
                    unfocusedLabelColor = SannaSecondaryText
                )
            )

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SannaPrimary,
                    contentColor = SannaOnPrimary
                )
            ) {
                Text("+ Agregar", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        // Doctores Agregados
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 30.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredMedicos) { med ->
                    MinimalCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Dr. ${med.nombre} ${med.apellido}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = SannaPrimary
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                HorizontalDivider(
                                    color = SannaSecondaryText.copy(alpha = 0.3f),
                                    thickness = 1.dp,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFD3F8EF),
                                    modifier = Modifier.height(22.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(Color(0xFF00796B), CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "En Turno",
                                            color = Color(0xFF004D40),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info",
                                        tint = SannaSecondaryText,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        "CMP: ${med.colegiatura} | Tel: ${med.telefono}",
                                        fontSize = 13.sp,
                                        color = SannaSecondaryText
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "Email",
                                        tint = SannaSecondaryText,
                                        modifier = Modifier.size(16.dp)
                                    )

                                    Text(
                                        med.email,
                                        fontSize = 13.sp,
                                        color = SannaSecondaryText
                                    )
                                }
                            }
                            IconButton(onClick = { viewModel.deleteMedico(med) }) {
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
                        viewModel.addMedico(
                            MedicoEntity(
                                nombre = nombre,
                                apellido = apellido,
                                colegiatura = cmp,
                                telefono = telefono,
                                email = email
                            )
                        )
                        nombre = ""; apellido = ""; cmp = ""; telefono = ""; email = ""
                        showDialog = false
                    }
                }) { Text("Guardar", color = SannaPrimary, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar", color = SannaSecondaryText)
                }
            },
            containerColor = SannaBackground
        )
    }
}
