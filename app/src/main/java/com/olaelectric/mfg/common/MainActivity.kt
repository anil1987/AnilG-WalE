package com.olaelectric.mfg.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.olaelectric.mfg.common.databinding.ActivityMainBinding
import com.olaelectric.mfg.grpc.GrpcService

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = MainActivity::class.simpleName.toString()
    }
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
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
                GrpcService.start(
                    this,
                    binding.hostEditText.text.toString(),
                    binding.portEditText.text.toString().toInt(),
                )
            } else {
                Toast.makeText(this, "Please enter the details", Toast.LENGTH_SHORT).show()
            }
        }
        binding.stop.setOnClickListener {
            GrpcService.stopService(this)
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(grpcReceiver, IntentFilter(GrpcService.ACTION_BROADCAST_SERVICE))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(grpcReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        GrpcService.stopService(this)
    }
}
