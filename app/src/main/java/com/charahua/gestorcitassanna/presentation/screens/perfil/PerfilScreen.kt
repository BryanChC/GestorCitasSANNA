package com.charahua.gestorcitassanna.presentation.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestorcitassanna.presentation.components.MinimalButton
import com.charahua.gestorcitassanna.presentation.components.MinimalCard
import com.charahua.gestorcitassanna.presentation.components.MinimalTextField
import com.charahua.gestorcitassanna.presentation.viewmodel.PacienteViewModel
import com.charahua.gestorcitassanna.ui.theme.*

@Composable
fun PerfilScreen(
    userId: Long,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember { PacienteViewModel(context.applicationContext as android.app.Application) }
    val usuario by viewModel.usuario.collectAsState()
    val paciente by viewModel.paciente.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var editTelefono by remember { mutableStateOf("") }
    var editEmail by remember { mutableStateOf("") }
    var editDireccion by remember { mutableStateOf("") }
    var editError by remember { mutableStateOf<String?>(null) }
    var editSuccessMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        viewModel.loadData(userId)
    }

    LaunchedEffect(usuario, paciente) {
        if (usuario != null && paciente != null) {
            editTelefono = usuario!!.telefono
            editEmail = usuario!!.email
            editDireccion = paciente!!.direccion
        }
    }

    // Diálogo de confirmación antes de cerrar sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.ExitToApp,
                    contentDescription = null,
                    tint = SannaPrimary
                )
            },
            title = {
                Text(
                    text = "Cerrar Sesión",
                    fontWeight = FontWeight.Bold,
                    color = SannaPrimary
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas salir de tu cuenta SANNA?",
                    color = SannaSecondaryText,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Sí, salir", color = SannaError, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar", color = SannaPrimary)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SannaBackground)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Mi Perfil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = SannaPrimary
        )
        Text(
            text = "Consulta y actualiza tus datos de contacto sanitario",
            fontSize = 13.sp,
            color = SannaSecondaryText
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (editSuccessMsg != null) {
            Surface(
                color = SannaSecondary,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = SannaSuccess,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = editSuccessMsg!!,
                        color = SannaSuccess,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        if (usuario != null && paciente != null) {
            // Cabecera con Avatar de iniciales
            val iniciales = "${usuario!!.nombre.firstOrNull() ?: ""}${usuario!!.apellido.firstOrNull() ?: ""}".uppercase()
            MinimalCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(SannaPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = iniciales,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${usuario!!.nombre} ${usuario!!.apellido}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = SannaPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = SannaSecondary,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Paciente Verificado SANNA",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = SannaPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta 1: Información Clínica
            MinimalCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "INFORMACIÓN CLÍNICA REGISTRADA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SannaSecondaryText
                        )
                        Surface(
                            color = SannaSecondary,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Lock,
                                    contentDescription = null,
                                    tint = SannaSecondaryText,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Protegido",
                                    fontSize = 10.sp,
                                    color = SannaSecondaryText,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    PerfilInfoItem(
                        icon = Icons.Outlined.Badge,
                        label = "DNI / Documento de Identidad",
                        value = usuario!!.dni
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = SannaSecondary)
                    PerfilInfoItem(
                        icon = Icons.Outlined.DateRange,
                        label = "Fecha de Nacimiento",
                        value = paciente!!.fechaNacimiento
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = SannaSecondary)
                    PerfilInfoItem(
                        icon = Icons.Outlined.Person,
                        label = "Género",
                        value = paciente!!.genero
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta 2: Datos de Contacto y Domicilio
            MinimalCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DATOS DE CONTACTO Y DOMICILIO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SannaSecondaryText
                        )
                        if (!isEditing) {
                            TextButton(
                                onClick = {
                                    isEditing = true
                                    editError = null
                                    editSuccessMsg = null
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = null,
                                    tint = SannaPrimary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Editar",
                                    color = SannaPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!isEditing) {
                        PerfilInfoItem(
                            icon = Icons.Outlined.Phone,
                            label = "Teléfono Celular",
                            value = usuario!!.telefono
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = SannaSecondary)
                        PerfilInfoItem(
                            icon = Icons.Outlined.Email,
                            label = "Correo Electrónico",
                            value = usuario!!.email
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = SannaSecondary)
                        PerfilInfoItem(
                            icon = Icons.Outlined.LocationOn,
                            label = "Dirección Domiciliaria",
                            value = paciente!!.direccion
                        )
                    } else {
                        MinimalTextField(
                            value = editTelefono,
                            onValueChange = { editTelefono = it },
                            label = "Teléfono Celular"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        MinimalTextField(
                            value = editEmail,
                            onValueChange = { editEmail = it },
                            label = "Correo Electrónico"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        MinimalTextField(
                            value = editDireccion,
                            onValueChange = { editDireccion = it },
                            label = "Dirección Domiciliaria"
                        )

                        if (editError != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = editError!!, color = SannaError, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            MinimalButton(
                                text = "Cancelar",
                                onClick = { isEditing = false; editError = null },
                                modifier = Modifier.weight(1f),
                                isPrimary = false
                            )
                            MinimalButton(
                                text = "Guardar Cambios",
                                onClick = {
                                    viewModel.updateProfile(
                                        userId = userId,
                                        telefono = editTelefono,
                                        email = editEmail,
                                        direccion = editDireccion,
                                        onResult = { ok, err ->
                                            if (ok) {
                                                isEditing = false
                                                editSuccessMsg = "Datos de contacto actualizados correctamente."
                                            } else {
                                                editError = err ?: "Error al actualizar perfil"
                                            }
                                        }
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        MinimalButton(
            text = "Cerrar Sesión",
            onClick = { showLogoutDialog = true },
            modifier = Modifier.fillMaxWidth(),
            isPrimary = false
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PerfilInfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = SannaSecondary,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SannaPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = SannaSecondaryText
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = SannaPrimary
            )
        }
    }
}
