package org.donnico.projectv1.auth.presentation.viewmodel

data class LoginState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false
)