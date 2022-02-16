package com.nasa.astronomy.data.nasa.repository

import com.nasa.astronomy.data.nasa.service.ApiInterface
import com.nasa.astronomy.domain.Constants
import com.nasa.astronomy.domain.nasa.AstroImageResult
import com.nasa.astronomy.domain.repository.IGetAstroImageRepository
import com.nasa.astronomy.domain.repository.ISharedPrefRepository
import com.nasa.astronomy.retrofit.network.Either
import com.nasa.astronomy.retrofit.network.networkerror.IFailure
import com.nasa.astronomy.retrofit.network.wrapper.safeApiCall

class GetAstroImageRepository(
    private val apiInterface: ApiInterface,
    private val sharedPrefRepository: ISharedPrefRepository
) : IGetAstroImageRepository {

    override suspend fun getAstroImageResult(currentDate: String): Either<IFailure, AstroImageResult> {
        val imageResultData = sharedPrefRepository.getAstroImgResult()
        val result = imageResultData.successValue()
        if (result != null) {
            if (currentDate == result.date) {
                return Either.Success(result)
            }
        }
        val apiResult = safeApiCall(
            apiCall = {
                apiInterface.astroImageInfo(Constants.API_KEY_VALUE, currentDate)
            },
            successTransform = {
                sharedPrefRepository.saveAstroImgResult(it)
                it
            }
        )
        // checking for api fail but last image details available
        if (apiResult.isError && result != null) {
            return Either.Success(result)
        }

        return apiResult
    }
}
