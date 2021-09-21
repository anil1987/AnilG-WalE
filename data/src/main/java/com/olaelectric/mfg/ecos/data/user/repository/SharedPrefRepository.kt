package com.olaelectric.mfg.ecos.data.user.repository

import android.content.Context
import com.etergo.domain.common.Either
import com.etergo.domain.common.UnitSuccess
import com.etergo.domain.error.IFailure
import com.olaelectric.mfg.ecos.domain.repository.ISharedPrefRepository
import com.olaelectric.mfg.ecos.domain.user.User

class SharedPrefRepository(context: Context) : ISharedPrefRepository {
    companion object {
        private const val TAG = "SharedPrefRepository"
        private const val PACKAGE_PREFIX_ = "com.olaelectric.mfg.ecos"
        private const val KEY_USER_NAME = "userName"
    }

    private val encryptedSharedPreferences by lazy {
        context.getSharedPreferences(PACKAGE_PREFIX_, Context.MODE_PRIVATE)
    }

    override fun saveUserName(userName: String): Either<IFailure, Unit> {
        encryptedSharedPreferences.edit().putString(KEY_USER_NAME, userName)
            .apply()
        return UnitSuccess
    }

    override fun getUserInfo(): Either<IFailure, User> {
        return Either.Success(
            User(
                encryptedSharedPreferences.getString(
                    KEY_USER_NAME,
                    ""
                ) ?: ""
            )
        )
    }
}
