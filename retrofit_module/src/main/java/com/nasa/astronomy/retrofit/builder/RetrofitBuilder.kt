package com.nasa.astronomy.retrofit.builder

import com.nasa.astronomy.retrofit.BuildConfig
import com.nasa.astronomy.retrofit.interceptor.BaseApiInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    fun <T : Any> create(
        baseUrl: String,
        apiInterface: Class<T>,
        apiInterceptor: BaseApiInterceptor,
    ): T {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(apiInterceptor)
        if (BuildConfig.DEBUG) {
            val interceptor =
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            okHttpClient.addInterceptor(interceptor)
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(apiInterface)
    }
}
