package com.nasa.astronomy.domain.repository

import com.nasa.astronomy.domain.nasa.AstroImageResult
import com.nasa.astronomy.retrofit.network.Either
import com.nasa.astronomy.retrofit.network.networkerror.IFailure

interface ISharedPrefRepository {
    fun saveAstroImgResult(imageResult: AstroImageResult): Either<IFailure, AstroImageResult>
    suspend fun getAstroImgResult(): Either<IFailure, AstroImageResult>
}
