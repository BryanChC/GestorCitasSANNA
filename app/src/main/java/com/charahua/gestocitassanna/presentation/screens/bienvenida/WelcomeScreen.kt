package com.charahua.gestocitassanna.presentation.screens.bienvenida

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestocitassanna.presentation.components.MinimalButton
import com.charahua.gestocitassanna.ui.theme.*

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit
) {
    var onboardingStep by remember { mutableStateOf(0) }

    val titles = listOf(
        "Bienvenido a SANNA Salud",
        "Gestión de Citas Médicas",
        "Atención de Calidad y Confianza"
    )

    val descriptions = listOf(
        "Tu red de salud privada líder en el Perú, ahora al alcance de tus manos con tecnología de vanguardia.",
        "Reserva tus citas con nuestros especialistas en nuestras diferentes sedes de manera rápida y segura.",
        "Monitorea tu historial médico, reprograma tus citas y accede a convenios con aseguradoras fácilmente."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SannaBackground)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SannaSecondary)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🏥 SANNA",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = SannaPrimary
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = titles[onboardingStep],
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = SannaPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = descriptions[onboardingStep],
                fontSize = 15.sp,
                color = SannaSecondaryText,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in titles.indices) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .width(if (i == onboardingStep) 24.dp else 8.dp)
                        .height(8.dp)
                        .background(
                            if (i == onboardingStep) SannaPrimary else SannaBorder,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
        }

        MinimalButton(
            text = if (onboardingStep < titles.size - 1) "Siguiente" else "Comenzar",
            onClick = {
                if (onboardingStep < titles.size - 1) {
                    onboardingStep++
                } else {
                    onGetStarted()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}
