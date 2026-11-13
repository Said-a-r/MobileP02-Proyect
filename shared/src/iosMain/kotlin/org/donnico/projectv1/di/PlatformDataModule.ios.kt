package org.donnico.projectv1.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.donnico.projectv1.data.local.DonNicoDatabase
import org.koin.dsl.module

actual fun platformDataModule() = module {
    single<SqlDriver> {
        NativeSqliteDriver(DonNicoDatabase.Schema, "donnico.db")
    }
}
