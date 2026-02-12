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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.edit

/**
 * UI state for the Home screen
 */
sealed interface SNUiState {

    object Idle : SNUiState
    object Loading : SNUiState
    object Success : SNUiState
    data class Error(val message: String) : SNUiState

}

class SNViewModel(private val snRepository: SNRepository, application: Application) : AndroidViewModel(application) {
    /** The mutable State that stores the status of the most recent request */
    var snUiState by mutableStateOf<SNUiState>(SNUiState.Idle)
        private set
    var alumno by mutableStateOf<com.example.siceproyect.data.Alumno?>(null)
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */




    fun login(matricula: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                snUiState = SNUiState.Loading
            }

            try {

                val loginResult = snRepository.acceso(matricula, password)

                if (!loginResult.success) {
                    withContext(Dispatchers.Main) {
                        snUiState = SNUiState.Error("Usuario o contraseña incorrectos")
                    }
                    return@launch
                }

                val alumnoParsed = snRepository.alumnoDatos()

                withContext(Dispatchers.Main) {
                    alumno = alumnoParsed
                    snUiState = SNUiState.Success
                }

            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    snUiState = SNUiState.Error("Error de conexión")
                }
            }
        }
    }
    fun logout() {
        val context = getApplication<Application>()

        context.getSharedPreferences("SicePrefs", Context.MODE_PRIVATE)
            .edit { clear() }

        context.getSharedPreferences("CookiePrefs", Context.MODE_PRIVATE)
            .edit { clear() }
        alumno = null
        snUiState = SNUiState.Idle
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
