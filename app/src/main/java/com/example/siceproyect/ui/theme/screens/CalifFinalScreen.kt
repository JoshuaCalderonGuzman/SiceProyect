package com.example.siceproyect.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.work.WorkInfo
import com.example.siceproyect.data.CalificacionFinal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// =====================================================================
// ENVOLTURA INTELIGENTE (OFFLINE-FIRST)
// =====================================================================
@Composable
fun FinalesScreen(viewModel: SNViewModel) {
    val context = LocalContext.current
    val uiState = viewModel.uiState
    val matricula = uiState.alumno?.matricula ?: return
    val modEducativo = uiState.alumno?.modEducativo?.toString() ?: return

    LaunchedEffect(Unit) {
        viewModel.cargarFinales(context, matricula, modEducativo)
    }

    val workInfos by viewModel.observarSaveFinalesWorker(context).observeAsState(emptyList())

    LaunchedEffect(workInfos) {
        val fetchInfo = workInfos.firstOrNull()
        if (fetchInfo?.state == WorkInfo.State.SUCCEEDED) {
            viewModel.cargarFinales(context, matricula, modEducativo)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        uiState.fechaActualizacionFinales?.let { timestamp ->
            val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
            Surface(color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.fillMaxWidth()) {
                Text("Última actualización: $fecha", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(16.dp, 8.dp))
            }
        }

        // Llamada a tu diseño original
        FinalesSection(calificaciones = uiState.califFinales ?: emptyList())
    }
}

// =====================================================================
// TU DISEÑO ORIGINAL (INTACTO)
// =====================================================================
@Composable
fun FinalesSection(calificaciones: List<CalificacionFinal>) {
    if (calificaciones.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.School,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp).alpha(0.3f)
                )
                Text(
                    "No hay calificaciones finales registradas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Placeholder si necesitas un header
        }
        items(calificaciones) { calif ->
            // Tu diseño de tarjeta (No incluí el interior de la tarjeta en este snippet
            // porque se cortaba en tu archivo, ¡pero tú tienes el código completo en tu Android Studio!)
            // Asegúrate de dejar TU código de "items(calificaciones)" completo aquí.
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(calif.materia, fontWeight = FontWeight.Bold)
                    Text("Calificación: ${calif.calif}")
                    Text("Acreditación: ${calif.acred}")
                    if (calif.observaciones.isNotBlank()) {
                        Text("Obs: ${calif.observaciones}")
                    }
                }
            }
        }
    }
}