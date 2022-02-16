package com.nasa.astronomy.base

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.nasa.astronomy.base.listeners.NetWorkListener
import com.nasa.astronomy.base.utils.NetWorkUtil

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    fun Context.showAlertDialog(
        msg: String,
        positiveClickListener: DialogInterface.OnClickListener? = null,
        positive: String? = null,
        negativeClickListener: DialogInterface.OnClickListener? = null,
        negative: String? = null,
        cancelable: Boolean = true
    ): AlertDialog {
        val alertDialog = AlertDialog.Builder(this).setMessage(msg)
        alertDialog.setCancelable(cancelable)
        if (positiveClickListener != null && !positive.isNullOrEmpty()) {
            alertDialog.setPositiveButton(
                positive,
                positiveClickListener
            )
        }
        if (negativeClickListener != null && !negative.isNullOrEmpty()) {
            alertDialog.setNegativeButton(
                negative,
                negativeClickListener
            )
        }
        return alertDialog.show()
    }

    companion object {
        val TAG = BaseFragment::class.simpleName
    }

    private var _binding: T? = null

    // This can be accessed by the child fragments
    // Only valid between onCreateView and onDestroyView
    val binding: T get() = _binding!!

    abstract fun getDataBinding(): T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getDataBinding()
        binding.lifecycleOwner = viewLifecycleOwner
        onBindView()
        return binding.root
    }

    abstract fun onBindView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onNetworkChange()
    }

    private val netWorkListener = object : NetWorkListener {
        override fun onConnectionAvailable() {
            onNetworkAvailable()
        }

        override fun onConnectionLost() {
            onNetworkLost()
        }
    }

    private fun onNetworkChange() {
        context?.let {
            NetWorkUtil.onNetWorkChange(it, netWorkListener)
        }
    }

    abstract fun onNetworkAvailable()
    abstract fun onNetworkLost()

    override fun onDestroy() {
        super.onDestroy()
        _binding?.unbind()
        _binding = null
    }
}
