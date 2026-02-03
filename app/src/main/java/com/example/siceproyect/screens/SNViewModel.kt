package com.example.siceproyect.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
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
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface SNUiState {
    data class Success(val accesoLogin: String) : SNUiState
    object Error : SNUiState
    object Loading : SNUiState
}

class SNViewModel(private val snRepository: SNRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var snUiState: SNUiState by mutableStateOf(SNUiState.Loading)
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        accesoSN()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    fun accesoSN() {
        viewModelScope.launch {
            snUiState = SNUiState.Loading
            try {
                val loginXml = withContext(Dispatchers.IO) {
                    snRepository.acceso("S22120150", "J/y5w9")
                }

                // snRepository.alumno_Datos()

                snUiState = SNUiState.Success(loginXml)

            } catch (e: Exception) {
                e.printStackTrace()
                snUiState = SNUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SICENETApplication)
                val snRepository = application.container.snRepository
                SNViewModel(snRepository = snRepository)
            }
        }
    }





}
