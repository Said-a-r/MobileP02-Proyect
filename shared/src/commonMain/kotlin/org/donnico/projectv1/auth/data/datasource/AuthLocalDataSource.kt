package org.donnico.projectv1.auth.data.datasource

import org.donnico.projectv1.auth.domain.model.User

class AuthLocalDataSource {
    suspend fun login(username: String, password: String): Result<User> =
        if (username == "admin" && password == "1234") {
            Result.success(User(username))
        } else {
            Result.failure(Exception("Usuario o contraseña incorrectos"))
        }
}