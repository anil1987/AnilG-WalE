package com.nasa.astronomy.retrofit.network

import com.google.gson.annotations.SerializedName

data class GenericResponse<T>(
    @SerializedName("status")
    val status: String,
    @SerializedName("title")
    val title: String?,
    @SerializedName("display_message")
    val displayMessage: String?,
    @SerializedName("data")
    val data: T
)
