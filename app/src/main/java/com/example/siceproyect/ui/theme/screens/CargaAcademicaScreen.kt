package com.example.siceproyect.ui.theme.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.siceproyect.data.MateriaCarga



@Composable
fun CargaAcademicaSection(lista: List<MateriaCarga>) {

    if (lista.isEmpty()) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val hoy = obtenerDiaActual()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // 🔥 TÍTULO HORARIO HOY
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Horario de Hoy ($hoy)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        val materiasHoy = lista.filter {
            it.estadoMateria == "O" &&
                    it.obtenerHorarioPorDia(hoy).isNotBlank()
        }

        if (materiasHoy.isEmpty()) {
            Text(
                "No tienes clases hoy 🎉",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            materiasHoy.forEach { materia ->
                HorarioHoyCard(materia, hoy)
            }
        }

        HorizontalDivider()

        // 📚 TÍTULO HORARIO SEMANAL
        Text(
            text = "Horario Semanal",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        lista.filter { it.estadoMateria == "O" }
            .forEach { materia ->
                HorarioSemanaCard(materia)
            }
    }
}



@Composable
fun HorarioHoyCard(materia: MateriaCarga, dia: String) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp) // Espaciado más fino
        ) {
            Text(
                text = materia.materia,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Grupo: ${materia.grupo}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(Modifier.height(8.dp))


            Text(
                text = "Docente: ${materia.docente}",
                style = MaterialTheme.typography.bodyMedium
            )


            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = materia.obtenerHorarioPorDia(dia),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}
@Composable
fun HorarioSemanaCard(materia: MateriaCarga) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = materia.materia,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Grupo: ${materia.grupo}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(Modifier.height(12.dp))


            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
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

@Composable
fun DiaRow(nombreDia: String, horario: String) {
    if (horario.isNotBlank()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = nombreDia,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = horario,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
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
        else -> "Domingo"
    }
}
