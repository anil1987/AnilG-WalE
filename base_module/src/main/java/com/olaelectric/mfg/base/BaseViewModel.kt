package com.olaelectric.mfg.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etergo.domain.interactor.UseCase
import com.olaelectric.mfg.base.networkerror.ErrorModel
import com.olaelectric.mfg.base.networkerror.Failure
import com.olaelectric.mfg.base.networkerror.IFailure
import com.olaelectric.mfg.logging.AppLogger
import kotlinx.coroutines.CoroutineScope

open class BaseViewModel : ViewModel() {

    companion object {
        private val TAG = BaseViewModel::class.java.simpleName.toString()
    }

    private val mErrorFailure = SingleLiveData<ErrorModel>()

    fun <Params, Type> UseCase<Params, Type>.invoke(

        params: Params,
        onSuccess: (Type) -> Unit = {},
        onFailure: (IFailure) -> Unit = ::failure,
        scope: CoroutineScope = viewModelScope,
        showBottomSheet: Boolean = true
    ) where Type : Any =
        this.invoke(
            scope = scope,
            params = params,
            onSuccess = onSuccess,
            onFailure = {
                if (showBottomSheet)
                    failure(it as IFailure)
                onFailure(it as IFailure)
            },
        )

    private fun failure(errorFailure: IFailure) {
        when (errorFailure) {
            is Failure.DisplayableErrors -> {
                updateErrorMessageForUser(errorFailure)
            }
        }
    }

    private fun updateErrorMessageForUser(errorFailure: IFailure) {
        val failure = errorFailure as com.olaelectric.mfg.base.networkerror.IFailure
        AppLogger.d(TAG, "Error occurred: ${failure.errorModel}")
        // common place to handle error if needed
        mErrorFailure.value = failure.errorModel
    }
}
