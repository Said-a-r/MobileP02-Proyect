package org.donnico.projectv1.di

import org.donnico.projectv1.auth.data.datasource.AuthLocalDataSource
import org.donnico.projectv1.auth.data.repository.AuthRepositoryImpl
import org.donnico.projectv1.auth.domain.repository.AuthRepository
import org.koin.dsl.module

val dataModule = module {
    single { AuthLocalDataSource() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}