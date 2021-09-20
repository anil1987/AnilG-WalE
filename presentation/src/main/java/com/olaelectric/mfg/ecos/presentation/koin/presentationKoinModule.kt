package com.olaelectric.mfg.ecos.presentation.koin

import com.olaelectric.mfg.ecos.presentation.router.NavigationRouter
import org.koin.dsl.module

val presentationKoinModule = module {
    single { NavigationRouter(get()) }
}
