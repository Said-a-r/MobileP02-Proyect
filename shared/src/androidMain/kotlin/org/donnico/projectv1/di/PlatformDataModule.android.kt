package org.donnico.projectv1.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.donnico.projectv1.data.local.DonNicoDatabase
import org.koin.dsl.module

actual fun platformDataModule() = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = DonNicoDatabase.Schema,
            context = get<Context>(),
            name = "donnico.db"
        )
    }
}
