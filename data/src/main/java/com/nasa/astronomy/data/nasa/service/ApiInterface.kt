package com.nasa.astronomy.data.nasa.service

import com.nasa.astronomy.domain.nasa.AstroImageResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    // Add api service related API here

    @GET(ApiConstant.API_IMAGE_INFO)
    suspend fun astroImageInfo(
        @Query(ApiConstant.API_PARAM_KEY) apiKeyValue: String,
        @Query(ApiConstant.API_PARAM_DATE) dateValue: String
    ): AstroImageResult
}
