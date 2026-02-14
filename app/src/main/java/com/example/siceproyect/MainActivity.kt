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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SiceProyectTheme {
                val snViewModel: SNViewModel =
                    viewModel(factory = SNViewModel.Factory)
                val uiState = snViewModel.uiState


                Scaffold { padding ->

                    if (uiState.isLogged) {

                        HomeScreen(
                            alumno = uiState.alumno,
                            padding = padding,
                            onLogout = { snViewModel.logout() }
                        )

                    } else {

                        LoginScreen(
                            uiState = uiState,
                            onLogin = { m, p -> snViewModel.login(m, p) }
                        )

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