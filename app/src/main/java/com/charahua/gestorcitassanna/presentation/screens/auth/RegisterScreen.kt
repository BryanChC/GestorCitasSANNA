package com.charahua.gestorcitassanna.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestorcitassanna.domain.validation.ValidationUtils
import com.charahua.gestorcitassanna.presentation.components.MinimalButton
import com.charahua.gestorcitassanna.presentation.components.MinimalTextField
import com.charahua.gestorcitassanna.presentation.viewmodel.AuthState
import com.charahua.gestorcitassanna.presentation.viewmodel.AuthViewModel
import com.charahua.gestorcitassanna.ui.theme.*

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: (String, Long, Long) -> Unit,
    onBackToLogin: () -> Unit
) {
    var step by remember { mutableStateOf(1) }

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var fechaNacimiento by remember { mutableStateOf("1998-01-01") }
    var genero by remember { mutableStateOf("Masculino") }
    var direccion by remember { mutableStateOf("") }

    var errorMsg by remember { mutableStateOf<String?>(null) }
    val authState by viewModel.uiState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            val success = authState as AuthState.Success
            onRegisterSuccess(success.rol, success.userId, success.pacienteId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SannaBackground)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro de Paciente",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = SannaPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Paso $step de 3",
            fontSize = 14.sp,
            color = SannaSecondaryText
        )
        Spacer(modifier = Modifier.height(24.dp))

        when (step) {
            1 -> {
                MinimalTextField(value = nombre, onValueChange = { nombre = it }, label = "Nombre")
                Spacer(modifier = Modifier.height(12.dp))
                MinimalTextField(value = apellido, onValueChange = { apellido = it }, label = "Apellido")
                Spacer(modifier = Modifier.height(12.dp))
                MinimalTextField(value = dni, onValueChange = { dni = it }, label = "DNI (8 dígitos)")
                Spacer(modifier = Modifier.height(12.dp))
                MinimalTextField(value = telefono, onValueChange = { telefono = it }, label = "Teléfono celular (999999999)")
            }
            2 -> {
                MinimalTextField(value = email, onValueChange = { email = it }, label = "Correo electrónico")
                Spacer(modifier = Modifier.height(12.dp))
                MinimalTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Contraseña (Mín. 6 caracteres)",
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Mostrar u ocultar contraseña",
                                tint = SannaSecondaryText
                            )
                        }
                    }
                )
            }
            3 -> {
                MinimalTextField(value = fechaNacimiento, onValueChange = { fechaNacimiento = it }, label = "Fecha de Nacimiento (YYYY-MM-DD)")
                Spacer(modifier = Modifier.height(12.dp))
                MinimalTextField(value = genero, onValueChange = { genero = it }, label = "Género")
                Spacer(modifier = Modifier.height(12.dp))
                MinimalTextField(value = direccion, onValueChange = { direccion = it }, label = "Dirección")
            }
        }

        if (errorMsg != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = errorMsg!!, color = SannaError, fontSize = 14.sp)
        }

        if (authState is AuthState.Error) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = (authState as AuthState.Error).message, color = SannaError, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (step > 1) {
                MinimalButton(
                    text = "Anterior",
                    onClick = { step--; errorMsg = null },
                    modifier = Modifier.weight(1f),
                    isPrimary = false
                )
            }
            MinimalButton(
                text = if (step < 3) "Siguiente" else "Registrarse",
                onClick = {
                    errorMsg = null
                    if (step == 1) {
                        if (nombre.isBlank() || apellido.isBlank()) {
                            errorMsg = "Complete nombre y apellido"
                            return@MinimalButton
                        }
                        if (!ValidationUtils.isValidDni(dni)) {
                            errorMsg = "DNI debe tener 8 dígitos"
                            return@MinimalButton
                        }
                        if (!ValidationUtils.isValidPhone(telefono)) {
                            errorMsg = "Teléfono inválido (9 dígitos empezando con 9)"
                            return@MinimalButton
                        }
                        step++
                    } else if (step == 2) {
                        if (!ValidationUtils.isValidEmail(email.trim())) {
                            errorMsg = "Correo electrónico inválido"
                            return@MinimalButton
                        }
                        if (!ValidationUtils.isValidPassword(password)) {
                            errorMsg = "Contraseña debe tener al menos 6 caracteres"
                            return@MinimalButton
                        }
                        step++
                    } else {
                        if (direccion.isBlank()) {
                            errorMsg = "Ingrese su dirección"
                            return@MinimalButton
                        }
                        viewModel.register(nombre, apellido, email.trim(), dni, telefono, password, fechaNacimiento, genero, direccion)
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBackToLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión", color = SannaPrimary, fontWeight = FontWeight.SemiBold)
        }
    }
}
