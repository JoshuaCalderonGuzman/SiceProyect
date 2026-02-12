package com.example.siceproyect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.siceproyect.ui.theme.SiceProyectTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.siceproyect.ui.theme.screens.HomeScreen
import com.example.siceproyect.ui.theme.screens.LoginScreen
import com.example.siceproyect.ui.theme.screens.SNViewModel
import com.example.siceproyect.ui.theme.screens.SNUiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SiceProyectTheme {
                val snViewModel: SNViewModel =
                    viewModel(factory = SNViewModel.Factory)



                Scaffold { padding ->

                    when (snViewModel.snUiState) {

                        is SNUiState.Idle,
                        is SNUiState.Error,
                        is SNUiState.Loading -> {

                            LoginScreen(snViewModel)

                        }

                        is SNUiState.Success -> {

                            HomeScreen(
                                snViewModel = snViewModel,
                                padding = padding
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