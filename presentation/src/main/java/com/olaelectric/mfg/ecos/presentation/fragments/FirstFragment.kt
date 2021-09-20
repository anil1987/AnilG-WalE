package com.olaelectric.mfg.ecos.presentation.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import com.olaelectric.mfg.base.BaseFragment
import com.olaelectric.mfg.ecos.presentation.databinding.FragmentFirstBinding
import com.olaelectric.mfg.grpc.GrpcService
import com.olaelectric.mfg.logging.AppLogger

class FirstFragment : BaseFragment<FragmentFirstBinding>() {
    companion object {
        val TAG = FirstFragment::class.simpleName.toString()
    }

    override fun getDataBinding(): FragmentFirstBinding {
        return FragmentFirstBinding.inflate(layoutInflater)
    }

    override fun onBindView() {
        initClickListeners()
        binding.hostEditText.setText("172.20.64.31")
        binding.portEditText.setText("50005")
    }

    private val grpcReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val isConnected = intent?.getBooleanExtra(GrpcService.CONNECTED, false) == true
            binding.status.text = if (isConnected) "Status: Connected" else "Status: Disconnected"
        }
    }

    private fun initClickListeners() {
        binding.connect.setOnClickListener {
            if (binding.hostEditText.text.isNotEmpty() && binding.portEditText.text.isNotEmpty()) {
                context?.let { it1 ->
                    GrpcService.start(
                        it1,
                        binding.hostEditText.text.toString(),
                        binding.portEditText.text.toString().toInt(),
                    )
                }
            } else {
                Toast.makeText(context, "Please enter the details", Toast.LENGTH_SHORT).show()
            }
        }
        binding.stop.setOnClickListener {
            context?.let { it1 -> GrpcService.stopService(it1) }
        }
    }

    override fun onResume() {
        super.onResume()
        context?.registerReceiver(grpcReceiver, IntentFilter(GrpcService.ACTION_BROADCAST_SERVICE))
    }

    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(grpcReceiver)
    }

    override fun onNetworkAvailable() {
        AppLogger.i(TAG, "onNetworkAvailable")
    }

    override fun onNetworkLost() {
        AppLogger.i(TAG, "onNetworkLost")
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.let { GrpcService.stopService(it) }
    }
}
