package com.example.siceproyect.ui.theme.screens

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.siceproyect.SICENETApplication
import com.example.siceproyect.data.SNRepository
import kotlinx.coroutines.launch
import androidx.core.content.edit
import androidx.work.WorkManager
import com.example.siceproyect.data.Alumno
import com.example.siceproyect.data.CalificacionFinal
import com.example.siceproyect.data.KardexCompleto
import com.example.siceproyect.data.MateriaCarga
import com.example.siceproyect.data.MateriaUnidades
import com.example.siceproyect.data.worker.lanzarLoginSync
import kotlinx.coroutines.async


/**
 * UI state for the Home screen
 */
data class SNUiState (

    val isLoading: Boolean = false,
    val isLogged: Boolean = false,
    val alumno: Alumno? = null,
    val kardex: KardexCompleto? = null,
    val califFinales: List<CalificacionFinal> = emptyList(),
    val califUnidades: List<MateriaUnidades> = emptyList(),
    val cargaAcademica: List<MateriaCarga> = emptyList(),
    val errorMessage: String? = null

)

class SNViewModel(private val snRepository: SNRepository, application: Application) : AndroidViewModel(application) {
    /** The mutable State that stores the status of the most recent request */
    var uiState by mutableStateOf(SNUiState())
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */




    fun login(matricula: String, password: String) {
        viewModelScope.launch {

            val context = getApplication<Application>()
            context.getSharedPreferences("CookiePrefs", Context.MODE_PRIVATE)
                .edit { clear() }
            uiState = uiState.copy(
                isLoading = true,
                errorMessage = null
            )

            try {

                val loginResult = snRepository.acceso(matricula, password)

                if (!loginResult.success) {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "Matrícula o contraseña incorrectos"
                    )
                    return@launch
                }
                val alumnoParsed = snRepository.alumnoDatos()
                kotlinx.coroutines.coroutineScope{
                    val kardexDeferred = async { snRepository.kardex(alumnoParsed.modEducativo) }
                    val califDataDeferred = async { snRepository.califFinal(alumnoParsed.modEducativo) }
                    val unidadesDeferred = async { snRepository.califUnidades() }
                    val cargaDeferred = async { snRepository.cargaAcademica() }

                    uiState = uiState.copy(
                        isLoading = false,
                        isLogged = true,
                        alumno = alumnoParsed,
                        kardex = kardexDeferred.await(),
                        califFinales = califDataDeferred.await(),
                        califUnidades = unidadesDeferred.await(),
                        cargaAcademica = cargaDeferred.await()
                    )

                }

            } catch (_: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error de conexión"
                )
            }
        }
    }
    fun loginOfflineFirst(context: Context, control: String, nip: String) {
        lanzarLoginSync(context, control, nip)
    }

    fun observarLogin(context: Context) =
        WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkLiveData("login_sync")
    fun logout() {
        val context = getApplication<Application>()

        context.getSharedPreferences("SicePrefs", Context.MODE_PRIVATE)
            .edit { clear() }

        context.getSharedPreferences("CookiePrefs", Context.MODE_PRIVATE)
            .edit { clear() }
        uiState = SNUiState()
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SICENETApplication)
                val snRepository = application.container.snRepository
                SNViewModel(snRepository = snRepository, application = application)
            }
        }
    }





}
