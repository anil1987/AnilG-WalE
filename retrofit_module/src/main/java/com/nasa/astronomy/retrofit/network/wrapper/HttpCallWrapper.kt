package com.nasa.astronomy.retrofit.network.wrapper

import com.google.gson.Gson
import com.nasa.astronomy.logging.AppLogger
import com.nasa.astronomy.retrofit.network.Either
import com.nasa.astronomy.retrofit.network.GenericResponse
import com.nasa.astronomy.retrofit.network.networkerror.ErrorDefaultMessage
import com.nasa.astronomy.retrofit.network.networkerror.ErrorModel
import com.nasa.astronomy.retrofit.network.networkerror.Failure
import com.nasa.astronomy.retrofit.network.networkerror.HttpConflictErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

suspend inline fun <T, X> safeApiCall(
    crossinline apiCall: suspend () -> T,
    successTransform: (T) -> X
): Either<Failure.HttpErrors, X> {
    return try {
        val response = withContext(Dispatchers.IO) { apiCall.invoke() }
        AppLogger.d("safeApiCall", "Api call successful")
        Either.Success(successTransform(response))
    } catch (e: Exception) {
        AppLogger.d("safeApiCall", "Exception occurred while making an http call}\n${e.message}")
        return when (e) {
            is HttpException -> {
                val errorMsg = getErrorMsg(e.response()?.errorBody()?.string())
                if (e.response()?.code() == 409) {
                    return Either.Error(
                        HttpConflictErrors.ConflictError(
                            errorMsg
                        )
                    )
                }
                Either.Error(
                    Failure.DisplayableErrors.Other(errorMsg)
                )
            }
            is SocketTimeoutException -> Either.Error(
                Failure.DisplayableErrors.Timeout(
                    ErrorModel(
                        errorDebugMsg = e.localizedMessage,
                        errorMsg = ErrorDefaultMessage.DEFAULT_ERROR_MSG.name
                    )
                )
            )
            is java.net.UnknownHostException -> Either.Error(
                Failure.DisplayableErrors.ConnectivityError(
                    ErrorModel(
                        errorDebugMsg = e.localizedMessage,
                        errorMsg = ErrorDefaultMessage.DEFAULT_ERROR_MSG.name
                    )
                )
            )
            is IOException -> Either.Error(
                Failure.DisplayableErrors.Network(
                    ErrorModel(
                        errorDebugMsg = e.localizedMessage,
                        errorMsg = ErrorDefaultMessage.DEFAULT_ERROR_MSG.name
                    )
                )
            )
            else -> Either.Error(
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
fun getErrorMsg(errorBody: String?): ErrorModel {
    return errorBody?.let { errorMsg ->

        try {
            val res = Gson().fromJson(errorMsg, GenericResponse::class.java)
            return ErrorModel(
                errorMsg = res.title ?: ErrorDefaultMessage.DEFAULT_ERROR_MSG.name,
                errorDebugMsg = errorBody
            )
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
            ErrorModel(
                errorMsg = ErrorDefaultMessage.DEFAULT_ERROR_MSG.name
            ) // in case if parsing failed -> show msg
        }
    }
        ?: ErrorModel(
            errorMsg = ErrorDefaultMessage.DEFAULT_ERROR_MSG.name
        ) // in case if backend is not sending proper error body -> show msg
}
