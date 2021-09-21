package com.olaelectric.mfg.ecos.data.koin

import com.olaelectric.mfg.ecos.data.user.repository.SharedPrefRepository
import com.olaelectric.mfg.ecos.domain.repository.ISharedPrefRepository
import org.koin.dsl.module

val koinSetupModule = module {
    single<ISharedPrefRepository> { SharedPrefRepository(get()) }
}

val dataKoinModule = listOf(
    koinSetupModule,
)
