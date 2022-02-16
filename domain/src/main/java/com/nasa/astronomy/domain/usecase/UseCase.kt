package com.nasa.astronomy.domain.usecase

import com.nasa.astronomy.retrofit.network.Either
import com.nasa.astronomy.retrofit.network.networkerror.IFailure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class UseCase<in Params, out Type> where Type : Any? {

    abstract suspend fun run(params: Params): Either<IFailure, Type>

    operator fun invoke(
        scope: CoroutineScope,
        params: Params,
        onSuccess: (Type) -> Unit = {},
        onFailure: (IFailure) -> Unit = {}
    ) {
        val job = scope.async { run(params) }
        scope.launch {
            job.await().either(onFailure, onSuccess)
        }
    }
}
