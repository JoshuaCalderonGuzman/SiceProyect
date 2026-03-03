package com.example.siceproyect.ui.theme.screens

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.siceproyect.SICENETApplication
import com.example.siceproyect.data.Alumno
import com.example.siceproyect.data.CalificacionFinal
import com.example.siceproyect.data.KardexCompleto
import com.example.siceproyect.data.MateriaCarga
import com.example.siceproyect.data.MateriaUnidades
import com.example.siceproyect.data.SNRepository
import com.example.siceproyect.data.repository.LocalRepository
import com.example.siceproyect.data.worker.lanzarSyncCargaAcademica
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

data class SNUiState (
    val isLoading: Boolean = false,
    val isLogged: Boolean = false,
    val alumno: Alumno? = null,
    val kardex: KardexCompleto? = null,
    val califFinales: List<CalificacionFinal>? = null,
    val califUnidades: List<MateriaUnidades>? = null,
    val cargaAcademica: List<MateriaCarga>? = null,
    //Para la fecha de última actualización
    val fechaActualizacionCarga: Long? = null,
    val errorMessage: String? = null,
    val fechaActualizacionKardex: Long? = null,
    val fechaActualizacionFinales: Long? = null,
    val fechaActualizacionUnidades: Long? = null
)

class SNViewModel(
    private val snRepository: SNRepository,
    private val localRepository: LocalRepository, // INYECTAMOS ROOM
    application: Application
) : AndroidViewModel(application) {

    var uiState by mutableStateOf(SNUiState())
        private set

    // Tu función de login original...
    fun login(m: String, p: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                // 1. INTENTO ONLINE (CON INTERNET)
                val result = snRepository.acceso(m, p)

                // 🔥 Asegúrate de usar la variable correcta aquí (ej. result.accesoLocal o result.acceso)
                if (result.success) {

                    val alumnoParsed = snRepository.alumnoDatos()

                    // --- GUARDAMOS LA SESIÓN EN ROOM (OFFLINE) ---
                    val jsonAlumno = Gson().toJson(alumnoParsed)
                    val alumnoEntity = com.example.siceproyect.data.local.entity.AlumnoEntity(
                        control = m, // Usamos la matrícula como llave primaria
                        jsonData = jsonAlumno,
                        ultimaActualizacion = System.currentTimeMillis()
                    )
                    localRepository.saveAlumno(alumnoEntity)
                    // ----------------------------------------------

                    // RESTAURAMOS LAS DESCARGAS DE LOS DEMÁS APARTADOS
                    val kardexDeferred = async { snRepository.kardex(alumnoParsed.modEducativo) }
                    val califDataDeferred = async { snRepository.califFinal(alumnoParsed.modEducativo) }
                    val unidadesDeferred = async { snRepository.califUnidades() }

                    uiState = uiState.copy(
                        isLogged = true,
                        isLoading = false,
                        alumno = alumnoParsed,
                        kardex = kardexDeferred.await(),
                        califFinales = califDataDeferred.await(),
                        califUnidades = unidadesDeferred.await()
                    )

                } else {
                    uiState = uiState.copy(isLoading = false, errorMessage = "Credenciales incorrectas")
                }
            } catch (e: Exception) {
                // 2. MODO OFFLINE (FALLÓ EL INTERNET)

                // Buscamos si el usuario ya había iniciado sesión antes
                val alumnoLocal = localRepository.getAlumno(m)

                if (alumnoLocal != null) {
                    // Si existe en la base de datos, lo leemos y entramos
                    val alumnoGuardado = Gson().fromJson(alumnoLocal.jsonData, Alumno::class.java)

                    uiState = uiState.copy(
                        isLogged = true,
                        isLoading = false,
                        alumno = alumnoGuardado,
                        errorMessage = "Modo sin conexión" // Opcional: le avisa al usuario
                    )
                } else {
                    // Si no hay internet y nunca había iniciado sesión, no lo dejamos entrar
                    uiState = uiState.copy(isLoading = false, errorMessage = "Sin conexión a internet")
                }
            }
        }
    }
    fun logout() {
        // Restablece el estado por completo, borrando los datos y marcando isLogged como false
        uiState = SNUiState()
    }

    //CARGA ACADÉMICA (OFFLINE FIRST)
    fun cargarCargaAcademica(context: Context, matricula: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            //Buscamos en Room primero (Sin Internet)
            val cargaLocal = localRepository.getCargaByControl(matricula)

            if (cargaLocal != null) {
                // Si hay datos guardados, los convertimos de JSON a Lista
                val tipoLista = object : TypeToken<List<MateriaCarga>>() {}.type
                val listaMaterias: List<MateriaCarga> = Gson().fromJson(cargaLocal.jsonData, tipoLista)

                // Actualizamos la UI inmediatamente
                uiState = uiState.copy(
                    cargaAcademica = listaMaterias,
                    fechaActualizacionCarga = cargaLocal.ultimaActualizacion,
                    isLoading = false
                )
            } else {
                uiState = uiState.copy(isLoading = false)
            }

            //Mandamos a los Workers a buscar internet y actualizar la BD
            lanzarSyncCargaAcademica(context, matricula)
        }
    }
    fun cargarKardex(context: Context, matricula: String, modEducativo: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            // Buscar en Room
            val kardexLocal = localRepository.getKardexByControl(matricula)

            if (kardexLocal != null) {
                val kardexGuardado = Gson().fromJson(kardexLocal.jsonData, KardexCompleto::class.java)
                uiState = uiState.copy(
                    kardex = kardexGuardado,
                    fechaActualizacionKardex = kardexLocal.ultimaActualizacion,
                    isLoading = false
                )
            } else {
                uiState = uiState.copy(isLoading = false)
            }

            //Lanzar Workers
            com.example.siceproyect.data.worker.lanzarSyncKardex(context, matricula, modEducativo)
        }
    }

    //CALIFICACIONES FINALES
    fun cargarFinales(context: Context, matricula: String, modEducativo: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            //Buscar en Room
            val finalesLocal = localRepository.getCalifFinalByControl(matricula)

            if (finalesLocal != null) {
                val tipoLista = object : TypeToken<List<CalificacionFinal>>() {}.type
                val listaFinales: List<CalificacionFinal> = Gson().fromJson(finalesLocal.jsonData, tipoLista)

                uiState = uiState.copy(
                    califFinales = listaFinales,
                    fechaActualizacionFinales = finalesLocal.ultimaActualizacion,
                    isLoading = false
                )
            } else {
                uiState = uiState.copy(isLoading = false)
            }

            //Lanzar Workers
            com.example.siceproyect.data.worker.lanzarSyncFinales(context, matricula, modEducativo)
        }
    }

    fun observarSaveFinalesWorker(context: Context): LiveData<List<WorkInfo>> {
        return WorkManager.getInstance(context).getWorkInfosByTagLiveData("SAVE_FINALES_TAG")
    }

    // CALIFICACIONES POR UNIDADES
    fun cargarUnidades(context: Context, matricula: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            //Buscar en Room
            val unidadesLocal = localRepository.getCalifUnidadesByControl(matricula)

            if (unidadesLocal != null) {
                val tipoLista = object : TypeToken<List<MateriaUnidades>>() {}.type
                val listaUnidades: List<MateriaUnidades> = Gson().fromJson(unidadesLocal.jsonData, tipoLista)

                uiState = uiState.copy(
                    califUnidades = listaUnidades,
                    fechaActualizacionUnidades = unidadesLocal.ultimaActualizacion,
                    isLoading = false
                )
            } else {
                uiState = uiState.copy(isLoading = false)
            }

            //Lanzar Workers
            com.example.siceproyect.data.worker.lanzarSyncUnidades(context, matricula)
        }
    }

    fun observarSaveUnidadesWorker(context: Context): LiveData<List<WorkInfo>> {
        return WorkManager.getInstance(context).getWorkInfosByTagLiveData("SAVE_UNIDADES_TAG")
    }
    fun observarFetchKardexWorker(context: Context) =
        WorkManager.getInstance(context).getWorkInfosByTagLiveData("FETCH_KARDEX_TAG")

    // Función para que la UI sepa cuándo el Worker terminó
    fun observarFetchCargaWorker(context: Context): LiveData<List<WorkInfo>> {
        return WorkManager.getInstance(context)
            .getWorkInfosByTagLiveData("FETCH_CARGA_TAG")
    }

    // FACTORY (PARA INYECTAR LOCAL REPOSITORY)
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SICENETApplication)
                val snRepository = application.container.snRepository
                val localRepository = application.container.localRepository

                SNViewModel(
                    snRepository = snRepository,
                    localRepository = localRepository,
                    application = application
                )
            }
        }
    }
}