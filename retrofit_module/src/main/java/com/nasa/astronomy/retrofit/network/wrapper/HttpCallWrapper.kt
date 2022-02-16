package com.nasa.astronomy.retrofit.network.wrapper

import com.google.gson.Gson
import com.nasa.astronomy.logging.AppLogger
import com.nasa.astronomy.retrofit.network.Either
import com.nasa.astronomy.retrofit.network.GenericResponse
import com.nasa.astronomy.retrofit.network.networkerror.ErrorDefaultMessage
import com.nasa.astronomy.retrofit.network.networkerror.ErrorModel
import com.nasa.astronomy.retrofit.network.networkerror.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

suspend inline fun <T, X> safeApiCall(
    crossinline apiCall: suspend () -> T,
    successTransform: (T) -> X
): Either<Failure.HttpErrors, X> {
    try {
        val response = withContext(Dispatchers.IO) { apiCall.invoke() }
        AppLogger.d("safeApiCall", "Api call successful")
        return Either.Success(successTransform(response))
    } catch (e: Exception) {
        AppLogger.d("safeApiCall", "Exception occurred while making an http call}\n${e.message}")
        if (e is HttpException) {
            val errorMsg = getErrorMsg(e)
            return Either.Error(
                Failure.DisplayableErrors.Other(errorMsg)
            )
        } else if (e is java.net.UnknownHostException) {
            return Either.Error(
                Failure.DisplayableErrors.ConnectivityError(
                    ErrorModel(
                        errorDebugMsg = e.localizedMessage,
                        errorMsg = ErrorDefaultMessage.DEFAULT_ERROR_MSG.name
                    )
                )
            )
        } else if (e is SocketTimeoutException) {
            return Either.Error(
                Failure.DisplayableErrors.Timeout(
                    ErrorModel(
                        errorDebugMsg = e.localizedMessage,
                        errorMsg = ErrorDefaultMessage.DEFAULT_ERROR_MSG.name
                    )
                )
            )
        } else {
            return Either.Error(
                Failure.DisplayableErrors.Unknown(
                    ErrorModel(
                        errorDebugMsg = e.localizedMessage,
                        errorMsg = ErrorDefaultMessage.DEFAULT_ERROR_MSG.name
                    )
                )
            )
        }
    }
}

// fetch msg from HttpException
fun getErrorMsg(error: HttpException?): ErrorModel {
    error?.let {
        try {
            val errorBody = error.response()?.errorBody()?.toString()
            val res = Gson().fromJson(errorBody, GenericResponse::class.java)
            return ErrorModel(
                errorMsg = res.msg ?: ErrorDefaultMessage.DEFAULT_ERROR_MSG.name,
                errorDebugMsg = errorBody
            )
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
            ErrorModel(
                errorMsg = ErrorDefaultMessage.DEFAULT_ERROR_MSG.name
            ) // in case if parsing failed -> show msg
        }
    }
    return ErrorModel(
        errorMsg = ErrorDefaultMessage.DEFAULT_ERROR_MSG.name
    ) // in case if backend is not sending proper error body -> show msg
}
