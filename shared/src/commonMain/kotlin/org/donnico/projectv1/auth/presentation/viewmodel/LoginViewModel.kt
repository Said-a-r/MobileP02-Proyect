package org.donnico.projectv1.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.donnico.projectv1.auth.domain.usecase.LoginUseCase

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.UsernameChanged -> _state.value = _state.value.copy(username = event.value)
            is LoginEvent.PasswordChanged -> _state.value = _state.value.copy(password = event.value)
            is LoginEvent.LoginClicked -> login()
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            val result = loginUseCase(_state.value.username, _state.value.password)
            result.fold(
                onSuccess = {
                    _state.value = _state.value.copy(loading = false, loginSuccess = true)
                },
                onFailure = { e ->
                    _state.value = _state.value.copy(loading = false, error = e.message)
                }
            )
        }
    }
}