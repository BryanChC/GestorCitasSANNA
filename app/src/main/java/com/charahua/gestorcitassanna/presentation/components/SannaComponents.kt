package com.charahua.gestorcitassanna.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestorcitassanna.ui.theme.*

@Composable
fun MinimalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isPrimary: Boolean = true
) {
    if (isPrimary) {
        Button(
            onClick = onClick,
            modifier = modifier.height(50.dp),
            enabled = enabled,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SannaPrimary,
                contentColor = SannaOnPrimary
            )
        ) {
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(50.dp),
            enabled = enabled,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, SannaBorder),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = SannaPrimary
            )
        ) {
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun MinimalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = isError,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SannaPrimary,
                unfocusedBorderColor = SannaBorder,
                errorBorderColor = SannaError,
                focusedLabelColor = SannaPrimary,
                unfocusedLabelColor = SannaSecondaryText
            )
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = SannaError,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun MinimalCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SannaSecondary),
        border = BorderStroke(1.dp, SannaBorder),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinimalTopBar(
    title: String,
    onBackClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SannaPrimary) },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Text("←", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = SannaPrimary)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = SannaBackground)
    )
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = SannaPrimary)
    }
}

@Composable
fun EmptyState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SannaPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = subtitle, fontSize = 14.sp, color = SannaSecondaryText)
    }
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirmar", color = SannaPrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = SannaSecondaryText)
            }
        },
        containerColor = SannaBackground,
        shape = RoundedCornerShape(16.dp)
    )
}
