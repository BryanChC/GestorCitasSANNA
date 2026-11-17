package com.charahua.gestocitassanna.presentation.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestocitassanna.presentation.components.MinimalButton
import com.charahua.gestocitassanna.presentation.components.MinimalCard
import com.charahua.gestocitassanna.presentation.components.MinimalTextField
import com.charahua.gestocitassanna.presentation.viewmodel.PacienteViewModel
import com.charahua.gestocitassanna.ui.theme.*

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SannaBackground)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Perfil del Paciente",
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
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            ) {
                Text(
                    text = editSuccessMsg!!,
                    color = SannaSuccess,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        if (usuario != null && paciente != null) {
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
                            Text(
                                text = "🔒 Protegido",
                                fontSize = 10.sp,
                                color = SannaSecondaryText,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "${usuario!!.nombre} ${usuario!!.apellido}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = SannaPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "DNI / Documento: ${usuario!!.dni}", fontSize = 14.sp, color = SannaPrimary)
                    Text(text = "Fecha de Nacimiento: ${paciente!!.fechaNacimiento}", fontSize = 14.sp, color = SannaPrimary)
                    Text(text = "Género: ${paciente!!.genero}", fontSize = 14.sp, color = SannaPrimary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                            TextButton(onClick = { isEditing = true; editError = null; editSuccessMsg = null }) {
                                Text("Editar Datos", color = SannaPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!isEditing) {
                        Text(text = "Teléfono celular: ${usuario!!.telefono}", fontSize = 14.sp, color = SannaPrimary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Correo electrónico: ${usuario!!.email}", fontSize = 14.sp, color = SannaPrimary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Dirección: ${paciente!!.direccion}", fontSize = 14.sp, color = SannaPrimary)
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
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            isPrimary = false
        )
    }
}
