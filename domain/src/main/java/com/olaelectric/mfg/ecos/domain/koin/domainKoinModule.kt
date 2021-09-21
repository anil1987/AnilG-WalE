package com.olaelectric.mfg.ecos.domain.koin

import com.olaelectric.mfg.ecos.domain.usecase.SaveUserUseCase
import org.koin.dsl.module

val koinSetupModule = module {
    single { SaveUserUseCase(get()) }
}

val domainKoinModule = listOf(
    koinSetupModule,
)
