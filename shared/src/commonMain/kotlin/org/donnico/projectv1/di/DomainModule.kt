package org.donnico.projectv1.di

import org.donnico.projectv1.auth.domain.usecase.LoginUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::LoginUseCase)
}