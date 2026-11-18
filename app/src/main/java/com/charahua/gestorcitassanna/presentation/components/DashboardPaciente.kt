package com.charahua.gestorcitassanna.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestorcitassanna.ui.theme.SannaOnPrimary
import com.charahua.gestorcitassanna.ui.theme.SannaPrimary
import androidx.compose.foundation.clickable

@Composable
fun DashboardPaciente(
    tieneCitas: Boolean,
    fecha: String,
    hora: String,
    motivo: String,
    onVerCitas: () -> Unit
) {

    val especialidades = listOf(
        "Cardiología",
        "Pediatría",
        "Neurología",
        "Oftalmología",
        "Traumatología"
    )

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = SannaPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Hola 👋",
                    color = SannaOnPrimary
                )

                Text(
                    text = "Bienvenido a SANNA",
                    color = SannaOnPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onVerCitas()
                }
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Próxima cita",
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                if (tieneCitas) {

                    Text(
                        text = "$fecha - $hora",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = motivo
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Consulta programada",
                        color = SannaPrimary
                    )
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Ver detalle de la cita",
                        color = SannaPrimary,
                        fontWeight = FontWeight.Bold
                    )

                } else {

                    Text(
                        text = "No tienes citas programadas"
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Agenda una consulta médica para empezar."
                    )

                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Especialidades Destacadas",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(especialidades) { especialidad ->

                Card {

                    Text(
                        text = especialidad,
                        modifier = Modifier.padding(12.dp)
                    )

                }

            }

        }
    }
}