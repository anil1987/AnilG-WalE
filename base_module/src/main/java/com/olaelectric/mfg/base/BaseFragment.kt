package com.olaelectric.mfg.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.olaelectric.mfg.base.listeners.NetWorkListener
import com.olaelectric.mfg.base.utils.NetWorkUtil

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

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
