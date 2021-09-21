package com.olaelectric.mfg.ecos.domain.usecase

import com.etergo.domain.common.Either
import com.etergo.domain.error.IFailure
import com.etergo.domain.interactor.UseCase
import com.olaelectric.mfg.ecos.domain.repository.ISharedPrefRepository

class SaveUserUseCase(
    private val sharedPrefRepository: ISharedPrefRepository
) : UseCase<String, Unit>() {

    override suspend fun run(params: String): Either<IFailure, Unit> {
        return sharedPrefRepository.saveUserName(params)
    }
}
