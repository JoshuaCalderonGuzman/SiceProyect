package com.example.siceproyect.ui.theme.screens

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.siceproyect.SICENETApplication
import com.example.siceproyect.data.SNRepository
import com.example.siceproyect.data.parseAlumno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * UI state for the Home screen
 */
sealed interface SNUiState {
    object Idle : SNUiState
    data class Success(val accesoLogin: String) : SNUiState
    object Error : SNUiState
    object Loading : SNUiState

}

class SNViewModel(private val snRepository: SNRepository, application: Application) : AndroidViewModel(application) {
    /** The mutable State that stores the status of the most recent request */
    var snUiState: SNUiState by mutableStateOf(SNUiState.Loading)
        private set
    var alumno by mutableStateOf<com.example.siceproyect.data.Alumno?>(null)
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */


    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */

    fun login(matricula: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    snUiState = SNUiState.Loading
                }

                getApplication<Application>()
                    .getSharedPreferences("SicePrefs", Context.MODE_PRIVATE)
                    .edit().clear().apply()

                val loginResult = snRepository.acceso(matricula, password)

                if (loginResult.contains("RECHAZADO", true) || loginResult.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        snUiState = SNUiState.Error
                    }
                    return@launch
                }

                getApplication<Application>()
                    .getSharedPreferences("SicePrefs", Context.MODE_PRIVATE)
                    .edit().putBoolean("logged", true).apply()

                val xmlAlumno = snRepository.alumno_Datos()
                withContext(Dispatchers.Main) {
                    alumno = parseAlumno(xmlAlumno)
                    snUiState = SNUiState.Success("OK")
                }

                withContext(Dispatchers.Main) {
                    snUiState = SNUiState.Success(xmlAlumno)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    snUiState = SNUiState.Error
                }
            }
        }
    }
    fun logout() {
        val context = getApplication<Application>()

        context.getSharedPreferences("SicePrefs", Context.MODE_PRIVATE)
            .edit().clear().apply()

        context.getSharedPreferences("CookiePrefs", Context.MODE_PRIVATE)
            .edit().clear().apply()

        snUiState = SNUiState.Loading
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
