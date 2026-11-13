package org.donnico.projectv1.di

import app.cash.sqldelight.db.SqlDriver
import org.donnico.projectv1.data.local.DonNicoDatabase
import org.donnico.projectv1.auth.data.datasource.AuthLocalDataSource
import org.donnico.projectv1.auth.data.repository.AuthRepositoryImpl
import org.donnico.projectv1.auth.domain.repository.AuthRepository
import org.donnico.projectv1.product.data.datasource.ProductLocalDataSource
import org.donnico.projectv1.product.data.repository.ProductRepositoryImpl
import org.donnico.projectv1.product.domain.repository.ProductRepository
import org.koin.dsl.module

val dataModule = module {
    single { DonNicoDatabase(get<SqlDriver>()) }

    single { AuthLocalDataSource() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    single { ProductLocalDataSource(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
}
