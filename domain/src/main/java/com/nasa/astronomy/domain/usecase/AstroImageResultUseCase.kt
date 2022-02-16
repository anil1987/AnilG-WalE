package com.nasa.astronomy.domain.usecase

import com.nasa.astronomy.domain.nasa.AstroImageResult
import com.nasa.astronomy.domain.repository.IGetAstroImageRepository
import com.nasa.astronomy.retrofit.network.Either
import com.nasa.astronomy.retrofit.network.networkerror.IFailure

class AstroImageResultUseCase(
    private val getAstroImageRepository: IGetAstroImageRepository
) : UseCase<String, AstroImageResult>() {

    override suspend fun run(params: String): Either<IFailure, AstroImageResult> {
        return getAstroImageRepository.getAstroImageResult(params)
    }
}
