package com.olaelectric.mfg.ecos.presentation.koin

import com.olaelectric.mfg.ecos.presentation.router.NavigationRouter
import com.olaelectric.mfg.ecos.presentation.viewmodels.FirstFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationKoinModule = module {
    single { NavigationRouter(get()) }
    viewModel {
        FirstFragmentViewModel(get(), get())
    }
}
