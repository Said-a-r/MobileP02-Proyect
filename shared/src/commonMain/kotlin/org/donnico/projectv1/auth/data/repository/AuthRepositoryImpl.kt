package org.donnico.projectv1.auth.data.repository

import org.donnico.projectv1.auth.data.datasource.AuthLocalDataSource
import org.donnico.projectv1.auth.domain.model.User
import org.donnico.projectv1.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(private val dataSource: AuthLocalDataSource) : AuthRepository {
    override suspend fun login(username: String, password: String): Result<User> =
        dataSource.login(username, password)
}