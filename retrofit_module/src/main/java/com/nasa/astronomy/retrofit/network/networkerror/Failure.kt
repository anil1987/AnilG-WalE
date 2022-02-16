package com.nasa.astronomy.retrofit.network.networkerror

interface IFailure {
    val errorModel: ErrorModel
}

sealed class Failure(override val errorModel: ErrorModel) :
    IFailure {
    open class FeatureFailure(
        error: ErrorModel = ErrorModel(errorMsg = "")
    ) : Failure(error)

    /**
     * This error class will be used for displaying the errors to the user
     */
    sealed class HttpErrors(error: ErrorModel) : FeatureFailure(error)

    /**
     * This error class will be used for displaying the error to the user
     */
    sealed class DisplayableErrors(error: ErrorModel) : HttpErrors(error) {
        class ConnectivityError(
            error: ErrorModel
        ) : DisplayableErrors(error)

        class Network(error: ErrorModel) :
            DisplayableErrors(error)

        class Timeout(error: ErrorModel) :
            DisplayableErrors(error)

        class Unknown(error: ErrorModel) :
            DisplayableErrors(error)

        class Other(error: ErrorModel) :
            DisplayableErrors(error)
    }
}

enum class ErrorDefaultMessage {
    DEFAULT_ERROR_MSG // Something went wrong
}

sealed class HttpConflictErrors(error: ErrorModel) : Failure.HttpErrors(error) {
    class ConflictError(error: ErrorModel) :
        HttpConflictErrors(error)
}
