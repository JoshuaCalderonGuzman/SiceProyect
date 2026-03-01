package com.example.siceproyect.ui.theme.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.work.WorkInfo
import com.example.siceproyect.data.MateriaCarga
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CargaAcademicaScreen(viewModel: SNViewModel) {
    val context = LocalContext.current
    val uiState = viewModel.uiState
    val matricula = uiState.alumno?.matricula

    // Solo cargamos si tenemos la matrícula del alumno
    if (matricula != null) {

        // 1. Al entrar a la pantalla, pedimos la carga
        LaunchedEffect(Unit) {
            viewModel.cargarCargaAcademica(context, matricula)
        }

        // 2. Observamos al Worker 1 (Fetch)
        val workInfos by viewModel.observarFetchCargaWorker(context).observeAsState(emptyList())

        // 3. Si el Worker dice "SUCCEEDED" (Éxito), volvemos a leer de la BD
        LaunchedEffect(workInfos) {
            val fetchInfo = workInfos.firstOrNull()
            if (fetchInfo?.state == WorkInfo.State.SUCCEEDED) {
                viewModel.cargarCargaAcademica(context, matricula)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // 4. Etiqueta de Última Actualización (Requisito cumplido)
        uiState.fechaActualizacionCarga?.let { timestamp ->
            val fechaFormateada = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Última actualización: $fechaFormateada",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        // Si está cargando y no hay datos aún, mostrar spinner
        if (uiState.isLoading && uiState.cargaAcademica.isNullOrEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Mostrar la interfaz con las materias
            CargaAcademicaSection(lista = uiState.cargaAcademica ?: emptyList())
        }
    }
}

// ------------------------------------------------------------------------
// Tu UI de cartas original (intacta)
// ------------------------------------------------------------------------
@Composable
fun CargaAcademicaSection(lista: List<MateriaCarga>) {
    if (lista.isEmpty()) return

    val hoy = obtenerDiaActual()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Text(text = "Horario de Hoy ($hoy)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        val materiasHoy = lista.filter { it.obtenerHorarioPorDia(hoy).isNotBlank() }

        if (materiasHoy.isEmpty()) {
            Text(text = "No tienes clases programadas para hoy.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            materiasHoy.forEach { materia -> MateriaCard(materia = materia, destacarDia = hoy) }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Carga Completa", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        lista.forEach { materia -> MateriaCard(materia = materia) }
    }
}

@Composable
fun MateriaCard(materia: MateriaCarga, destacarDia: String? = null) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = materia.materia, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = "Docente: ${materia.docente}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (destacarDia != null) {
                    DiaRow(destacarDia.substring(0, 3), materia.obtenerHorarioPorDia(destacarDia), true)
                } else {
                    DiaRow("Lun", materia.lunes)
                    DiaRow("Mar", materia.martes)
                    DiaRow("Mié", materia.miercoles)
                    DiaRow("Jue", materia.jueves)
                    DiaRow("Vie", materia.viernes)
                    if (materia.sabado.isNotBlank()) DiaRow("Sáb", materia.sabado)
                }
            }
        }
    }
}

@Composable
fun DiaRow(nombreDia: String, horario: String, destacado: Boolean = false) {
    if (horario.isNotBlank()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = nombreDia, style = MaterialTheme.typography.bodyMedium, fontWeight = if (destacado) FontWeight.Bold else FontWeight.SemiBold, color = if (destacado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
            Text(text = horario, style = MaterialTheme.typography.bodyMedium, color = if (destacado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = if (destacado) FontWeight.Bold else FontWeight.Normal)
        }
    }
}

fun obtenerDiaActual(): String {
    val calendar = java.util.Calendar.getInstance()
    return when (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
        java.util.Calendar.MONDAY -> "Lunes"
        java.util.Calendar.TUESDAY -> "Martes"
        java.util.Calendar.WEDNESDAY -> "Miercoles"
        java.util.Calendar.THURSDAY -> "Jueves"
        java.util.Calendar.FRIDAY -> "Viernes"
        java.util.Calendar.SATURDAY -> "Sabado"
        else -> ""
    }
}