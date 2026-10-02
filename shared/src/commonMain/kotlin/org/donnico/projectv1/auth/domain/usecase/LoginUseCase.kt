package org.donnico.projectv1.auth.domain.usecase

import org.donnico.projectv1.auth.domain.model.User
import org.donnico.projectv1.auth.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): Result<User> =
        repository.login(username, password)
}