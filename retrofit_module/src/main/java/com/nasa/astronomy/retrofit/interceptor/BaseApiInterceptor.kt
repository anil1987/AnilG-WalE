package com.nasa.astronomy.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

abstract class BaseApiInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = addInterceptor(chain.request().newBuilder())
        return chain.proceed(request)
    }

    abstract fun addInterceptor(interceptorBuilder: Request.Builder): Request
}
