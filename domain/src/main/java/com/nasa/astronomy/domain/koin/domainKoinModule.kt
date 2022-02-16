package com.nasa.astronomy.domain.koin

import com.nasa.astronomy.domain.usecase.AstroImageResultUseCase
import org.koin.dsl.module

val koinSetupModule = module {
    single { AstroImageResultUseCase(get()) }
}

val domainKoinModule = listOf(
    koinSetupModule,
)
