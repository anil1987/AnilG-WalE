package com.nasa.astronomy.domain.repository

import com.nasa.astronomy.domain.nasa.AstroImageResult
import com.nasa.astronomy.retrofit.network.Either
import com.nasa.astronomy.retrofit.network.networkerror.IFailure

interface IGetAstroImageRepository {
    suspend fun getAstroImageResult(currentDate: String): Either<IFailure, AstroImageResult>
}
