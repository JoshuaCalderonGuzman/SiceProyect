package com.example.siceproyect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.siceproyect.ui.theme.SiceProyectTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.siceproyect.screens.SNViewModel
import com.example.siceproyect.screens.SNUiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SiceProyectTheme {
                val snViewModel: SNViewModel =
                    viewModel(factory = SNViewModel.Factory)

                val uiState = snViewModel.snUiState

                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    when (uiState) {
                        is SNUiState.Loading -> {
                            Text(
                                text = "Cargando...",
                                modifier = Modifier.padding(padding)
                            )
                        }

                        is SNUiState.Success -> {
                            Text(
                                text = "LOGIN OK\n\n${uiState.accesoLogin}",
                                modifier = Modifier.padding(padding)
                            )
                        }

                        is SNUiState.Error -> {
                            Text(
                                text = "ERROR en SICENET",
                                modifier = Modifier.padding(padding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SiceProyectTheme {
        Greeting("Android")
    }
}