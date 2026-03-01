package com.example.siceproyect.ui.theme.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.work.WorkInfo
import com.example.siceproyect.data.MateriaUnidades
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// =====================================================================
// ENVOLTURA INTELIGENTE (OFFLINE-FIRST)
// =====================================================================
@Composable
fun UnidadesScreen(viewModel: SNViewModel) {
    val context = LocalContext.current
    val uiState = viewModel.uiState
    val matricula = uiState.alumno?.matricula ?: return

    LaunchedEffect(Unit) {
        viewModel.cargarUnidades(context, matricula)
    }

    val workInfos by viewModel.observarSaveUnidadesWorker(context).observeAsState(emptyList())

    LaunchedEffect(workInfos) {
        val fetchInfo = workInfos.firstOrNull()
        if (fetchInfo?.state == WorkInfo.State.SUCCEEDED) {
            viewModel.cargarUnidades(context, matricula)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        uiState.fechaActualizacionUnidades?.let { timestamp ->
            val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
            Surface(color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.fillMaxWidth()) {
                Text("Última actualización: $fecha", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(16.dp, 8.dp))
            }
        }

        // Llamada a tu diseño original
        UnidadesSection(materias = uiState.califUnidades ?: emptyList())
    }
}

// =====================================================================
// TU DISEÑO ORIGINAL (INTACTO)
// =====================================================================
@Composable
fun UnidadesSection(materias: List<MateriaUnidades>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(materias) { materia ->
            UnidadesCard(materia)
        }
    }
}

@Composable
fun UnidadesCard(materia: MateriaUnidades) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(materia.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Grupo: ${materia.grupo}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)

            Spacer(Modifier.height(12.dp))

            // Flujo horizontal de unidades
            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                materia.calificaciones.forEachIndexed { index, calif ->
                    if (calif != null) { // Solo mostrar las que tienen valor (no null)
                        UnidadBadge(numero = index + 1, nota = calif)
                    }
                }
            }
        }
    }
}

@Composable
fun UnidadBadge(numero: Int, nota: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // El número de unidad
        Text("U$numero", style = MaterialTheme.typography.labelSmall)
        // El círculo con la nota
        Surface(
            modifier = Modifier.size(38.dp),
            shape = CircleShape,
            color = if (nota >= 70) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = nota.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}