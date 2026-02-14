package com.example.siceproyect.ui.theme.screens

import android.app.Application
import android.content.Context
import android.util.Log
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
import com.example.siceproyect.data.Alumno

/**
 * UI state for the Home screen
 */
data class SNUiState (

    val isLoading: Boolean = false,
    val isLogged: Boolean = false,
    val alumno: Alumno? = null,
    val errorMessage: String? = null

)

class SNViewModel(private val snRepository: SNRepository, application: Application) : AndroidViewModel(application) {
    /** The mutable State that stores the status of the most recent request */
    var uiState by mutableStateOf<SNUiState>(SNUiState())
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
                        errorMessage = "Usuario o contraseña incorrectos"
                    )
                    return@launch
                }

                val alumnoParsed = snRepository.alumnoDatos()

                uiState = uiState.copy(
                    isLoading = false,
                    isLogged = true,
                    alumno = alumnoParsed
                )


            } catch (_: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error de conexión"
                )
            }
        }
    }
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
