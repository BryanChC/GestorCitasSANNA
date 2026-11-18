package com.charahua.gestorcitassanna.presentation.screens.citas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charahua.gestorcitassanna.data.local.entity.MedicoEntity
import com.charahua.gestorcitassanna.presentation.viewmodel.CitasViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarMedicoScreen(
    viewModel: CitasViewModel,
    pacienteId: Long,
    onBackToReservar: () -> Unit
) {
    // 1. RECOLECTAMOS LOS DATOS DE ROOM
    val medicos by viewModel.medicos.collectAsState()
    val especialidades by viewModel.especialidades.collectAsState()
    val sedes by viewModel.sedes.collectAsState()
    val medicoEspecialidades by viewModel.medicoEspecialidades.collectAsState()
    val medicoSedes by viewModel.medicoSedes.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var medicoSeleccionado by remember { mutableStateOf<MedicoEntity?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadBookingData()
        viewModel.loadAllMedicos()
    }

    // Funciones para cruzar los datos relacionales de Room
    val getEspecialidadParaMedico = { medicoId: Long ->
        val relacion = medicoEspecialidades.find { it.medicoId == medicoId }
        val especialidad = especialidades.find { it.id == relacion?.especialidadId }
        Pair(especialidad?.id ?: 1L, especialidad?.nombre ?: "ESPECIALISTA")
    }

    val getSedeParaMedico = { medicoId: Long ->
        val relacion = medicoSedes.find { it.medicoId == medicoId }
        val sede = sedes.find { it.id == relacion?.sedeId }
        Pair(sede?.id ?: 1L, sede?.nombre ?: "Sede Principal")
    }


    if (medicoSeleccionado == null) {

        val medicosFiltrados = medicos.filter {
            it.nombre.contains(searchQuery, ignoreCase = true) ||
                    it.apellido.contains(searchQuery, ignoreCase = true) ||
                    it.colegiatura.contains(searchQuery, ignoreCase = true)
        }

        Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
            HeaderMontefiori()
            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)

            Column(modifier = Modifier.padding(horizontal = 20.dp).fillMaxSize()) {
                Spacer(modifier = Modifier.height(24.dp))
                StepperVisual(pasoActivo = 2)
                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Busca a tu médico o\nespecialidad",
                    fontSize = 26.sp, fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2C3E50), textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(), lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Ingresa tu consulta (médico o CMP)", color = Color.Gray, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF005BAC),
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))
                Text("Mostrando ${medicosFiltrados.size} de ${medicos.size} médicos", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(medicosFiltrados) { med ->
                        val especialidad = getEspecialidadParaMedico(med.id)
                        val sede = getSedeParaMedico(med.id)

                        TarjetaMedico(
                            medico = med,
                            especialidadNombre = especialidad.second,
                            sedeNombre = sede.second,
                            onAgendarClick = { medicoSeleccionado = med }
                        )
                    }
                }
            }
        }
    } else {

        var fechaSeleccionada by remember { mutableStateOf(LocalDate.now()) }
        var horaSeleccionada by remember { mutableStateOf("") }

        val horariosManana = listOf("09:00", "09:40", "10:20", "11:00", "11:40")
        val horariosTarde = listOf("14:20", "15:00", "15:40", "16:20", "17:00")

        Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
            HeaderMontefiori()
            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)

            Column(modifier = Modifier.padding(horizontal = 20.dp).fillMaxSize().verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.height(24.dp))
                StepperVisual(pasoActivo = 3)
                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Selecciona fecha y\nhora de cita",
                    fontSize = 26.sp, fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2C3E50), textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(), lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(24.dp))

                val especialidad = getEspecialidadParaMedico(medicoSeleccionado!!.id)
                val sede = getSedeParaMedico(medicoSeleccionado!!.id)

                TarjetaMedico(
                    medico = medicoSeleccionado!!,
                    especialidadNombre = especialidad.second,
                    sedeNombre = sede.second,
                    onAgendarClick = {},
                    isResumen = true
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text("Fechas disponibles", fontSize = 14.sp, color = Color(0xFF005BAC), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                CalendarioHorizontal(
                    fechaSeleccionada = fechaSeleccionada,
                    onFechaSeleccionada = {
                        fechaSeleccionada = it
                        horaSeleccionada = ""
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                val formatoDia = fechaSeleccionada.format(DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM", Locale.forLanguageTag("es-PE"))).uppercase()

                Text("Horarios para el $formatoDia - MAÑANA", fontSize = 13.sp, color = Color(0xFF005BAC), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                GridHorariosDinamicos(horarios = horariosManana, horaSeleccionada = horaSeleccionada, onHoraClick = { horaSeleccionada = it })

                Spacer(modifier = Modifier.height(24.dp))

                Text("TURNO TARDE", fontSize = 13.sp, color = Color(0xFF005BAC), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                GridHorariosDinamicos(horarios = horariosTarde, horaSeleccionada = horaSeleccionada, onHoraClick = { horaSeleccionada = it })

                Spacer(modifier = Modifier.height(40.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                    OutlinedButton(
                        onClick = { medicoSeleccionado = null },
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(1.dp, Color.LightGray)
                    ) {
                        Text("Volver")
                    }

                    Button(
                        onClick = {
                            if (horaSeleccionada.isNotEmpty()) {
                                viewModel.reservarCita(
                                    pacienteId = pacienteId,
                                    medicoId = medicoSeleccionado!!.id,
                                    sedeId = sede.first,
                                    especialidadId = especialidad.first,
                                    fecha = fechaSeleccionada.format(DateTimeFormatter.ISO_DATE),
                                    hora = horaSeleccionada,
                                    motivo = "Consulta generada vía Directorio",
                                    aseguradoraId = null
                                )
                                onBackToReservar()
                            }
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        enabled = horaSeleccionada.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005BAC))
                    ) {
                        Text(if (horaSeleccionada.isEmpty()) "Elige una hora" else "Confirmar Cita", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun GridHorariosDinamicos(horarios: List<String>, horaSeleccionada: String, onHoraClick: (String) -> Unit) {
    horarios.chunked(3).forEach { filaHoras ->
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
            filaHoras.forEach { hora ->
                TimeChip(hora = hora, isSelected = horaSeleccionada == hora, onClick = onHoraClick, modifier = Modifier.weight(1f))
            }
            repeat(3 - filaHoras.size) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CalendarioHorizontal(fechaSeleccionada: LocalDate, onFechaSeleccionada: (LocalDate) -> Unit) {
    val hoy = LocalDate.now()
    val proximosDias = (0..14).map { hoy.plusDays(it.toLong()) }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        items(proximosDias) { fecha ->
            val isSelected = fecha == fechaSeleccionada
            val colorFondo = if (isSelected) Color(0xFF005BAC) else Color.White
            val colorTexto = if (isSelected) Color.White else Color.DarkGray
            val colorBorde = if (isSelected) Color(0xFF005BAC) else Color.LightGray

            Surface(
                modifier = Modifier.width(64.dp).clickable { onFechaSeleccionada(fecha) },
                shape = RoundedCornerShape(12.dp), color = colorFondo, border = BorderStroke(1.dp, colorBorde),
                shadowElevation = if (isSelected) 4.dp else 0.dp
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 12.dp)) {
                    val diaSemana = fecha.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.forLanguageTag("es-PE")).uppercase()
                    Text(text = diaSemana, fontSize = 12.sp, color = if(isSelected) Color(0xCCFFFFFF) else Color.Gray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = fecha.dayOfMonth.toString(), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = colorTexto)
                }
            }
        }
    }
}

@Composable
fun TarjetaMedico(medico: MedicoEntity, especialidadNombre: String, sedeNombre: String, onAgendarClick: () -> Unit, isResumen: Boolean = false) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if(isResumen) 0.dp else 2.dp),
        border = if (isResumen) BorderStroke(1.dp, Color(0xFFEEEEEE)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = Color(0xFF95A5A6), modifier = Modifier.size(60.dp)) {
                    Icon(Icons.Default.Person, contentDescription = "Avatar", tint = Color.White, modifier = Modifier.padding(12.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "${medico.apellido.uppercase()},\n${medico.nombre}", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color(0xFF2C3E50), lineHeight = 20.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(especialidadNombre.uppercase(), fontSize = 12.sp, color = Color(0xFF005BAC), fontWeight = FontWeight.Bold)

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = sedeNombre, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                    }
                    Text("CMP: ${medico.colegiatura}", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 2.dp))
                }
            }
            if (!isResumen) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onAgendarClick,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2C3E50), containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, Color(0xFFBDC3C7))
                ) {
                    Text("Agendar una cita", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun HeaderMontefiori() {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocalHospital, contentDescription = "Logo", tint = Color(0xFF005BAC), modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("CLÍNICA\nSANNA", fontWeight = FontWeight.ExtraBold, color = Color.DarkGray, fontSize = 16.sp, lineHeight = 16.sp)
        }
        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.DarkGray, modifier = Modifier.size(28.dp))
    }
}

@Composable
fun StepperVisual(pasoActivo: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        StepIcon(icon = Icons.Default.Person, isActive = pasoActivo == 1)
        StepLine()
        StepIcon(icon = Icons.Default.Search, isActive = pasoActivo == 2)
        StepLine()
        StepIcon(icon = Icons.Default.DateRange, isActive = pasoActivo == 3)
        StepLine()
        StepIcon(icon = Icons.Default.CheckCircle, isActive = pasoActivo == 4)
    }
}

@Composable
fun StepIcon(icon: ImageVector, isActive: Boolean) {
    val bgColor = if (isActive) Color(0xFF005BAC) else Color.Transparent
    val iconColor = if (isActive) Color.White else Color(0xFFBDC3C7)
    val borderColor = if (isActive) Color(0xFF005BAC) else Color(0xFFBDC3C7)
    Surface(shape = CircleShape, color = bgColor, border = BorderStroke(1.dp, borderColor), modifier = Modifier.size(40.dp)) {
        Box(contentAlignment = Alignment.Center) { Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp)) }
    }
}

@Composable
fun StepLine() {
    HorizontalDivider(color = Color(0xFFBDC3C7), thickness = 1.dp, modifier = Modifier.width(30.dp))
}

@Composable
fun TimeChip(hora: String, isSelected: Boolean, onClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.clickable { onClick(hora) },
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) Color(0xFF005BAC) else Color.White,
        border = BorderStroke(1.dp, if (isSelected) Color(0xFF005BAC) else Color.LightGray)
    ) {
        Text(hora, modifier = Modifier.padding(vertical = 12.dp), textAlign = TextAlign.Center, color = if (isSelected) Color.White else Color.DarkGray, fontWeight = FontWeight.Bold)
    }
}