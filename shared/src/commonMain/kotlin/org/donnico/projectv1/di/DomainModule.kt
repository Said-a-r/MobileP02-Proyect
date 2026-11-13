package org.donnico.projectv1.di

import org.donnico.projectv1.auth.domain.usecase.LoginUseCase
import org.donnico.projectv1.product.domain.usecase.FindProductByBarcodeUseCase
import org.donnico.projectv1.product.domain.usecase.SaveProductUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::LoginUseCase)
    singleOf(::FindProductByBarcodeUseCase)
    singleOf(::SaveProductUseCase)
}
