package com.nasa.astronomy.presentation.fragments

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nasa.astronomy.base.BaseFragment
import com.nasa.astronomy.presentation.R
import com.nasa.astronomy.presentation.databinding.FragmentFirstBinding
import com.nasa.astronomy.presentation.viewmodels.FirstFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FirstFragment : BaseFragment<FragmentFirstBinding>() {
    companion object {
    }
    private fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
        liveData.observe(this, Observer(body))
    }
    private val viewModel: FirstFragmentViewModel by viewModel()

    override fun getDataBinding(): FragmentFirstBinding {
        return FragmentFirstBinding.inflate(layoutInflater)
    }

    override fun onBindView() {
        viewModel.getAstroImageResult()
        initObservers()
    }

    private fun initObservers() {
        observe(viewModel.astroImageDetails) { astroImageDetails ->
            if (astroImageDetails != null) {
                binding.astroImageDetails = astroImageDetails
                if (viewModel.networkNotConnected.value == true) {
                    if (viewModel.todayDate != astroImageDetails.date) {
                        showInfoDialog(getString(R.string.str_unable_to_load_error))
                    }
                }
            } else {
                val info: String = if (viewModel.networkNotConnected.value == true) {
                    getString(R.string.str_load_error)
                } else {
                    getString(R.string.str_unable_to_fetch_details)
                }
                showInfoDialog(info, true)
            }
        }
    }

    private fun showInfoDialog(info: String, close: Boolean = false) {
        context?.let { currContext ->
            currContext.showAlertDialog(
                info,
                { dialog, _ ->
                    if (close) {
                        activity?.finish()
                    }
                    dialog.dismiss()
                },
                getString(R.string.str_ok), cancelable = false
            )
        }
    }

    override fun onNetworkAvailable() {
        viewModel.onNetworkAvailable()
    }

    override fun onNetworkLost() {
        viewModel.onNetworkLost()
    }
}
