package com.charahua.gestorcitassanna.presentation.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerfilScreen(
    userId: Long,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val viewModel = remember { PacienteViewModel(context.applicationContext as android.app.Application) }
    val emergenciaPrefs = remember { EmergenciaPreferences(context.applicationContext) }

    val usuario by viewModel.usuario.collectAsState()
    val paciente by viewModel.paciente.collectAsState()
    val datosEmergencia by emergenciaPrefs.observarDatosEmergencia(userId).collectAsState(initial = DatosEmergencia())

    var isEditing by remember { mutableStateOf(false) }
    var isEditingEmergencia by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    var editTelefono by remember { mutableStateOf("") }
    var editEmail by remember { mutableStateOf("") }
    var editDireccion by remember { mutableStateOf("") }
    var editError by remember { mutableStateOf<String?>(null) }
    var editSuccessMsg by remember { mutableStateOf<String?>(null) }

    // Estados para Ficha de Emergencia (DataStore)
    var editGrupoSanguineo by remember { mutableStateOf("") }
    var editAlergias by remember { mutableStateOf("") }
    var editContactoNombre by remember { mutableStateOf("") }
    var editContactoTelefono by remember { mutableStateOf("") }
    var emergenciaError by remember { mutableStateOf<String?>(null) }

    val gruposSanguineos = listOf("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-")

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

    LaunchedEffect(datosEmergencia) {
        editGrupoSanguineo = datosEmergencia.grupoSanguineo
        editAlergias = if (datosEmergencia.alergias == "Ninguna registrada") "" else datosEmergencia.alergias
        editContactoNombre = datosEmergencia.contactoNombre
        editContactoTelefono = datosEmergencia.contactoTelefono
    }

    // Cálculo dinámico del porcentaje de completitud del perfil sanitario
    val porcentajeCompletitud = remember(usuario, paciente, datosEmergencia) {
        if (usuario == null || paciente == null) 0
        else {
            var puntos = 0
            val totalCampos = 6
            if (usuario!!.telefono.isNotBlank()) puntos++
            if (usuario!!.email.isNotBlank()) puntos++
            if (paciente!!.direccion.isNotBlank()) puntos++
            if (datosEmergencia.grupoSanguineo.isNotBlank()) puntos++
            if (datosEmergencia.contactoNombre.isNotBlank()) puntos++
            if (datosEmergencia.contactoTelefono.isNotBlank()) puntos++
            (puntos * 100) / totalCampos
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
            // Cabecera con Avatar de iniciales y progreso de perfil
            val iniciales = "${usuario!!.nombre.firstOrNull() ?: ""}${usuario!!.apellido.firstOrNull() ?: ""}".uppercase()
            MinimalCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (porcentajeCompletitud == 100) "Perfil sanitario completo" else "Completa tu ficha de emergencia",
                            fontSize = 11.sp,
                            color = SannaSecondaryText,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "$porcentajeCompletitud%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (porcentajeCompletitud == 100) SannaSuccess else SannaPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { porcentajeCompletitud / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (porcentajeCompletitud == 100) SannaSuccess else SannaPrimary,
                        trackColor = SannaSecondary
                    )
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

            // Tarjeta 2: Ficha de Emergencia Médica (Persistencia real en DataStore)
            MinimalCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "FICHA DE EMERGENCIA MÉDICA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SannaSecondaryText
                        )
                        if (!isEditingEmergencia) {
                            TextButton(
                                onClick = {
                                    isEditingEmergencia = true
                                    emergenciaError = null
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
                                    text = if (datosEmergencia.grupoSanguineo.isBlank()) "Configurar" else "Editar",
                                    color = SannaPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!isEditingEmergencia) {
                        PerfilInfoItem(
                            icon = Icons.Outlined.FavoriteBorder,
                            label = "Grupo Sanguíneo y Factor Rh",
                            value = datosEmergencia.grupoSanguineo.ifBlank { "Sin especificar" }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = SannaSecondary)
                        PerfilInfoItem(
                            icon = Icons.Outlined.WarningAmber,
                            label = "Alergias Conocidas",
                            value = datosEmergencia.alergias.ifBlank { "Ninguna registrada" }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = SannaSecondary)
                        PerfilInfoItem(
                            icon = Icons.Outlined.ContactPhone,
                            label = "Contacto de Emergencia",
                            value = if (datosEmergencia.contactoNombre.isNotBlank()) {
                                "${datosEmergencia.contactoNombre} (${datosEmergencia.contactoTelefono})"
                            } else {
                                "Sin contacto asignado"
                            }
                        )
                    } else {
                        Text(
                            text = "Selecciona tu Grupo Sanguíneo:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = SannaPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            gruposSanguineos.forEach { grupo ->
                                val seleccionado = editGrupoSanguineo == grupo
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (seleccionado) SannaPrimary else Color.White)
                                        .border(
                                            width = 1.dp,
                                            color = if (seleccionado) SannaPrimary else SannaSecondaryText.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { editGrupoSanguineo = grupo }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = grupo,
                                        color = if (seleccionado) Color.White else SannaPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        MinimalTextField(
                            value = editAlergias,
                            onValueChange = { editAlergias = it },
                            label = "Alergias (ej. Penicilina, Ninguna)"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        MinimalTextField(
                            value = editContactoNombre,
                            onValueChange = { editContactoNombre = it },
                            label = "Nombre del Contacto de Emergencia"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        MinimalTextField(
                            value = editContactoTelefono,
                            onValueChange = { editContactoTelefono = it },
                            label = "Teléfono de Emergencia (9 dígitos)"
                        )

                        if (emergenciaError != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = emergenciaError!!, color = SannaError, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            MinimalButton(
                                text = "Cancelar",
                                onClick = {
                                    isEditingEmergencia = false
                                    emergenciaError = null
                                },
                                modifier = Modifier.weight(1f),
                                isPrimary = false
                            )
                            MinimalButton(
                                text = "Guardar Ficha",
                                onClick = {
                                    val telLimpio = editContactoTelefono.trim()
                                    when {
                                        editGrupoSanguineo.isBlank() -> {
                                            emergenciaError = "Selecciona tu grupo sanguíneo."
                                        }
                                        editContactoNombre.trim().length < 3 -> {
                                            emergenciaError = "Ingresa el nombre del contacto de emergencia."
                                        }
                                        !telLimpio.matches(Regex("^9\\d{8}$")) -> {
                                            emergenciaError = "El teléfono de emergencia debe tener 9 dígitos y empezar con 9."
                                        }
                                        else -> {
                                            emergenciaError = null
                                            scope.launch {
                                                emergenciaPrefs.guardarDatosEmergencia(
                                                    userId = userId,
                                                    grupoSanguineo = editGrupoSanguineo,
                                                    alergias = editAlergias,
                                                    contactoNombre = editContactoNombre,
                                                    contactoTelefono = telLimpio
                                                )
                                                isEditingEmergencia = false
                                                editSuccessMsg = "Ficha de emergencia guardada localmente."
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta 3: Datos de Contacto y Domicilio
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
