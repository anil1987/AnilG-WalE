package com.olaelectric.mfg.grpc

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.olaelectric.mfg.grpc.managers.GrpcManager
import com.olaelectric.mfg.logging.AppLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GrpcService : Service() {

    companion object {
        const val ACTION_BROADCAST_SERVICE = "com.olaelectric.mfg.action.ACTION_BROADCAST_SERVICE"
        val TAG = GrpcService::class.simpleName.toString()
        const val CONNECTED = "CONNECTED"
        const val IP_ADDRESS = "IP_ADDRESS"
        const val PORT = "PORT"
        const val INVALID_PORT = -1
        const val RESPONSE = "RESPONSE"
        const val NOTIFICATION_ID_1 = 1
        const val NOTIFICATION_ID_2 = 2

        fun start(context: Context, ipAddress: String, port: Int) {
            val intent = Intent(context, GrpcService::class.java)
            intent.putExtra(IP_ADDRESS, ipAddress)
            intent.putExtra(PORT, port)
            ContextCompat.startForegroundService(context, intent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, GrpcService::class.java)
            context.stopService(stopIntent)
        }
    }

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    private var ipAddress: String? = null
    private var portNo: Int = INVALID_PORT

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID_2, GrpcManager.getChannelNotification(this@GrpcService))
        } else {
            startForeground(NOTIFICATION_ID_1, Notification())
        }
    }

    override fun onCreate() {
        super.onCreate()
        AppLogger.i(TAG, "onCreate")
        createNotification()
        GrpcManager.initParserEngine(this@GrpcService)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        AppLogger.i(TAG, "onStartCommand")
        intent?.let {
            ipAddress = intent.getStringExtra(IP_ADDRESS)
            portNo = intent.getIntExtra(PORT, portNo)
        }

        if (!ipAddress.isNullOrEmpty() && portNo != INVALID_PORT) {
            GrpcManager.createChannel(this@GrpcService, ipAddress.toString(), portNo)
            scope.launch {
                while (isActive) {
                    try {
                        GrpcManager.startDataStreaming()
                    } catch (e: Exception) {
                        AppLogger.e(TAG, "Exception while data streaming", e)
                        stopSelf()
                    }
                }
            }
        } else {
            AppLogger.i(TAG, "ipAddress and portNo should not be empty")
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        scope.cancel()
        GrpcManager.shutdown()
        stopForeground(true)
        GrpcManager.sendConnectionBroadcast(this@GrpcService)
        AppLogger.i(TAG, "onDestroy")
        super.onDestroy()
    }
}
