package org.donnico.projectv1

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.donnico.projectv1.auth.presentation.screen.LoginScreen
import org.donnico.projectv1.auth.presentation.viewmodel.LoginViewModel
import org.donnico.projectv1.home.presentation.screen.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val viewModel: LoginViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    MaterialTheme {
        if (state.loginSuccess) {
            HomeScreen()
        } else {
            LoginScreen(onLoginSuccess = {})
        }
    }
}