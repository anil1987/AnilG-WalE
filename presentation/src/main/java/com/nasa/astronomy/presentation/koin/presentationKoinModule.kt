package com.nasa.astronomy.presentation.koin

import com.nasa.astronomy.presentation.viewmodels.FirstFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationKoinModule = module {
    viewModel {
        FirstFragmentViewModel(get())
    }
}
