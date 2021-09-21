package com.olaelectric.mfg.base.networkerror

interface IFailure : com.etergo.domain.error.IFailure {
    val errorModel: ErrorModel
}

sealed class Failure(override val errorModel: ErrorModel) :
    IFailure {
    open class FeatureFailure(
        error: ErrorModel = ErrorModel(errorMsg = ""),
        override val errorMessage: String = error.errorMsg
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
            error: ErrorModel,
            override val errorMessage: String = error.errorMsg
        ) : DisplayableErrors(error)

        class Network(error: ErrorModel, override val errorMessage: String = error.errorMsg) :
            DisplayableErrors(error)

        class Timeout(error: ErrorModel, override val errorMessage: String = error.errorMsg) :
            DisplayableErrors(error)

        class Unknown(error: ErrorModel, override val errorMessage: String = error.errorMsg) :
            DisplayableErrors(error)

        class Other(error: ErrorModel, override val errorMessage: String = error.errorMsg) :
            DisplayableErrors(error)
    }
}
