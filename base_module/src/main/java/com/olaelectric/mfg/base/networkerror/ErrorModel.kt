package com.olaelectric.mfg.base.networkerror

data class ErrorModel(
    val errorMsg: String,
    val errorDebugMsg: String? = null
)

enum class ErrorDefaultMessage {
    DEFAULT_ERROR_MSG // Something went wrong
}
