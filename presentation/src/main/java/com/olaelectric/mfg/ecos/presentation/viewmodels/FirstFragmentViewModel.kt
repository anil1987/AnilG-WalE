package com.olaelectric.mfg.ecos.presentation.viewmodels

import com.olaelectric.mfg.base.BaseViewModel
import com.olaelectric.mfg.ecos.domain.usecase.SaveUserUseCase
import com.olaelectric.mfg.ecos.presentation.router.NavigationRouter
import com.olaelectric.mfg.logging.AppLogger

class FirstFragmentViewModel(
    private val navigationRouter: NavigationRouter,
    private val saveUserUseCase: SaveUserUseCase
) : BaseViewModel() {

    companion object {
        private val TAG = FirstFragmentViewModel::class.simpleName.toString()
    }

    fun saveUser(userName: String) {
        saveUserUseCase.invoke(
            params = userName,
            onSuccess = {
                AppLogger.i(TAG, "User saved successfully")
            },
            onFailure = {
                AppLogger.i(TAG, it.errorMessage)
            }
        )
    }
}
