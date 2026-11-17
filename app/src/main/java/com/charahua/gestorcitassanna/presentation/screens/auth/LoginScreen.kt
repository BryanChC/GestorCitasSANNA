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
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: (String, Long, Long) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val authState by viewModel.uiState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            val success = authState as AuthState.Success
            onLoginSuccess(success.rol, success.userId, success.pacienteId)
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
            text = "Iniciar Sesión",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = SannaPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ingresa a tu cuenta SANNA",
            fontSize = 14.sp,
            color = SannaSecondaryText
        )
        Spacer(modifier = Modifier.height(32.dp))

        MinimalTextField(
            value = email,
            onValueChange = { email = it; emailError = null },
            label = "Correo electrónico",
            isError = emailError != null,
            errorMessage = emailError
        )

        Spacer(modifier = Modifier.height(16.dp))

        MinimalTextField(
            value = password,
            onValueChange = { password = it; passwordError = null },
            label = "Contraseña",
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Mostrar u ocultar contraseña",
                        tint = SannaSecondaryText
                    )
                }
            },
            isError = passwordError != null,
            errorMessage = passwordError
        )

        if (authState is AuthState.Error) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = (authState as AuthState.Error).message,
                color = SannaError,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        MinimalButton(
            text = "Iniciar Sesión",
            onClick = {
                var hasError = false
                val cleanEmail = email.trim()
                if (!ValidationUtils.isValidEmail(cleanEmail)) {
                    emailError = "Correo electrónico inválido"
                    hasError = true
                }
                if (password.isBlank()) {
                    passwordError = "Ingrese su contraseña"
                    hasError = true
                }
                if (!hasError) {
                    viewModel.login(cleanEmail, password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes una cuenta? Regístrate aquí", color = SannaPrimary, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = SannaSecondary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Credenciales por defecto:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SannaPrimary)
                Text("Admin: ryax@gmail.com / Sheldon25*", fontSize = 11.sp, color = SannaSecondaryText)
                Text("Paciente: usuario@gmail.com / Contrasena1*", fontSize = 11.sp, color = SannaSecondaryText)
            }
        }
    }
}
