package com.nasa.astronomy.data.koin

import com.google.gson.Gson
import com.nasa.astronomy.data.nasa.repository.GetAstroImageRepository
import com.nasa.astronomy.data.nasa.repository.SharedPrefRepository
import com.nasa.astronomy.data.nasa.service.ApiInterceptor
import com.nasa.astronomy.data.nasa.service.ApiInterface
import com.nasa.astronomy.domain.Constants
import com.nasa.astronomy.domain.repository.IGetAstroImageRepository
import com.nasa.astronomy.domain.repository.ISharedPrefRepository
import com.nasa.astronomy.retrofit.builder.RetrofitBuilder
import org.koin.dsl.module

val koinSetupModule = module {
    single { Gson() }
    single { ApiInterceptor() }
    single<ISharedPrefRepository> { SharedPrefRepository(get(), get()) }
    single {
        RetrofitBuilder.create(
            Constants.API_BASE_URL, ApiInterface::class.java, get<ApiInterceptor>()
        )
    }
    single<IGetAstroImageRepository> { GetAstroImageRepository(get(), get()) }
}

val dataKoinModule = listOf(
    koinSetupModule,
)
