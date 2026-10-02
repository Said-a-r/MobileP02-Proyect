package org.donnico.projectv1.auth.domain.repository

import org.donnico.projectv1.auth.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
}