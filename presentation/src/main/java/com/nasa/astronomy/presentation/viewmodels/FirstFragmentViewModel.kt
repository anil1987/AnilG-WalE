package com.nasa.astronomy.presentation.viewmodels

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasa.astronomy.domain.nasa.AstroImageResult
import com.nasa.astronomy.domain.usecase.AstroImageResultUseCase
import com.nasa.astronomy.logging.AppLogger
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class FirstFragmentViewModel(
    private val astroImageResultUseCase: AstroImageResultUseCase
) : ViewModel() {

    private val _astroImageDetails: MutableLiveData<AstroImageResult> = MutableLiveData()
    val astroImageDetails: LiveData<AstroImageResult> = _astroImageDetails

    private val _networkNotConnected: MutableLiveData<Boolean> = MutableLiveData(true)
    val networkNotConnected: LiveData<Boolean> = _networkNotConnected

    val todayDate: String by lazy {
        getCurrentFormalDate()
    }

    companion object {
        private val TAG = FirstFragmentViewModel::class.simpleName.toString()
        private const val DATE_TIME_FORMAT = "yyyy-MM-dd"
    }
    fun getAstroImageResult() {
        astroImageResultUseCase.invoke(
            params = todayDate,
            scope = viewModelScope,
            onSuccess = {
                _astroImageDetails.postValue(it)
                AppLogger.i(TAG, it.toString())
            },
            onFailure = {
                AppLogger.i(TAG, "Fail")
                _astroImageDetails.postValue(null)
            }
        )
    }

    fun onNetworkAvailable() {
        AppLogger.i(TAG, "onNetworkAvailable")
        _networkNotConnected.postValue(false)
    }

    fun onNetworkLost() {
        AppLogger.i(TAG, "onNetworkLost")
        _networkNotConnected.postValue(true)
    }

    private fun getCurrentFormalDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
        } else {
            val dateFormat = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
            dateFormat.format(Date())
        }
    }
}
