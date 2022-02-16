package com.nasa.astronomy.retrofit.network

import com.google.gson.annotations.SerializedName

data class GenericResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String?,
)
