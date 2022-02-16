package com.nasa.astronomy.data.nasa.service

import com.nasa.astronomy.retrofit.interceptor.BaseApiInterceptor
import okhttp3.Request

class ApiInterceptor : BaseApiInterceptor() {
    override fun addInterceptor(interceptorBuilder: Request.Builder): Request {
        return interceptorBuilder.build()
    }
}
