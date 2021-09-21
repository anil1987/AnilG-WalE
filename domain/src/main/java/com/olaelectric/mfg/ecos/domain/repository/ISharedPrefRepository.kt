package com.olaelectric.mfg.ecos.domain.repository

import com.etergo.domain.common.Either
import com.etergo.domain.error.IFailure
import com.olaelectric.mfg.ecos.domain.user.User

interface ISharedPrefRepository {
    fun saveUserName(userName: String): Either<IFailure, Unit>
    fun getUserInfo(): Either<IFailure, User>
}
