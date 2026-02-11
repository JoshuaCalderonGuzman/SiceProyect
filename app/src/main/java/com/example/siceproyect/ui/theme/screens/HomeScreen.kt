package com.example.siceproyect.ui.theme.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun HomeScreen(
    snViewModel: SNViewModel,
    padding: PaddingValues
) {
    val alumno = snViewModel.alumno

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(24.dp)
    ) {

        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        if (alumno != null) {
            Text("Nombre: ${alumno.nombre}")
            Text("Carrera: ${alumno.carrera}")
            Text("Matrícula: ${alumno.matricula}")
            Text("Inscrito: ${if (alumno.inscrito) "Sí" else "No"}")
            Text("Estatus: ${alumno.estatus}")
            Text("Semestre actual: ${alumno.semActual}")
            Text("Créditos acumulados: ${alumno.cdtosAcumulados}")
            Text("Créditos actuales: ${alumno.cdtosActuales}")
            Text("Especialidad: ${alumno.especialidad}")
            Text("Lineamiento: ${alumno.liniamiento}")
            Text("Fecha de reinscripción: ${alumno.fechaReins}")
            Text("Modo educativo: ${alumno.modEducativo}")
            Text("Adeudo: ${if (alumno.adeudo) "Sí" else "No"}")
            Text("Adeudo descriptivo: ${alumno.adeudoDescriptivo}")
            Text("URL de la foto: ${alumno.urlFoto}")

        } else {
            Text("Cargando datos del alumno...")
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = { snViewModel.logout() }) {
            Text("Cerrar sesión")
        }
    }
}
