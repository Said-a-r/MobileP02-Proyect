package org.donnico.projectv1.di

import android.content.Context
import androidx.room.Room
import org.donnico.projectv1.product.data.datasource.ProductLocalDataSource
import org.donnico.projectv1.product.data.local.DonNicoRoomDatabase
import org.donnico.projectv1.product.data.repository.ProductRepositoryImpl
import org.donnico.projectv1.product.domain.repository.ProductRepository
import org.koin.dsl.module

actual fun platformDataModule() = module {
    single {
        Room.databaseBuilder(
            get<Context>(),
            DonNicoRoomDatabase::class.java,
            "donnico_room.db"
        ).build()
    }

    single { get<DonNicoRoomDatabase>().productDao() }
    single { ProductLocalDataSource(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
}
