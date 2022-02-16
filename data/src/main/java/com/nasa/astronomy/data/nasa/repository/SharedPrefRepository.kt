package com.nasa.astronomy.data.nasa.repository

import android.content.Context
import com.google.gson.Gson
import com.nasa.astronomy.domain.nasa.AstroImageResult
import com.nasa.astronomy.domain.repository.ISharedPrefRepository
import com.nasa.astronomy.retrofit.network.Either
import com.nasa.astronomy.retrofit.network.networkerror.ErrorModel
import com.nasa.astronomy.retrofit.network.networkerror.Failure
import com.nasa.astronomy.retrofit.network.networkerror.IFailure

class SharedPrefRepository(
    context: Context,
    private val gson: Gson
) : ISharedPrefRepository {
    companion object {
        private const val PACKAGE_PREFIX_ = "com.nasa.astronomy"
        private const val ASTRO_IMG_DETAILS = "astroImageDetails"
    }

    private val encryptedSharedPreferences by lazy {
        context.getSharedPreferences(PACKAGE_PREFIX_, Context.MODE_PRIVATE)
    }

    override fun saveAstroImgResult(imageResult: AstroImageResult): Either<IFailure, AstroImageResult> {
        return try {
            val valueString = gson.toJson(imageResult)
            encryptedSharedPreferences.edit()
                .putString(ASTRO_IMG_DETAILS, valueString)
                .apply()
            Either.Success(imageResult)
        } catch (e: Exception) {
            Either.Error(Failure.FeatureFailure())
        }
    }

    override suspend fun getAstroImgResult(): Either<IFailure, AstroImageResult> {
        val extractedString = encryptedSharedPreferences.getString(
            ASTRO_IMG_DETAILS,
            null
        )
        return if (extractedString == null) {
            Either.Error(Failure.FeatureFailure(ErrorModel("Failed to fetch session id")))
        } else {
            try {
                val result =
                    gson.fromJson(extractedString, AstroImageResult::class.java)
                Either.Success(result)
            } catch (e: Exception) {
                Either.Error(Failure.FeatureFailure())
            }
        }
    }
}
