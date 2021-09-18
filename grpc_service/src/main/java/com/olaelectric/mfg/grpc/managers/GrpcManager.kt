package com.olaelectric.mfg.grpc.managers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.protobuf.ByteString
import com.google.protobuf.Empty
import com.olaelectric.dataparserengine.ParserEngine
import com.olaelectric.dataparserengine.can.ByteHelper
import com.olaelectric.dataparserengine.helpers.ConfigConstructor
import com.olaelectric.mfg.grpc.GrpcService.Companion.ACTION_BROADCAST_SERVICE
import com.olaelectric.mfg.grpc.GrpcService.Companion.CONNECTED
import com.olaelectric.mfg.grpc.R
import com.olaelectric.mfg.grpc.ScooterStatus
import com.olaelectric.mfg.grpc.helpers.JsonReaderHelper
import com.olaelectric.mfg.grpc.wifican.CANPacket
import com.olaelectric.mfg.grpc.wifican.WifiCanRpcGrpc
import com.olaelectric.mfg.logging.AppLogger
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.util.concurrent.TimeUnit

object GrpcManager {

    private val TAG = GrpcManager::class.simpleName.toString()
    private const val CHANNEL_NAME = "Grpc Server Background Service"

    private var channel: ManagedChannel? = null
    private val parserEngine = ParserEngine(ConfigConstructor())

    var ipAddress: String? = null
    var portNo: Int = -1

    @RequiresApi(Build.VERSION_CODES.O)
    fun getChannelNotification(context: Context): Notification {
        val channelId = context.packageName
        val notificationChannel = NotificationChannel(
            channelId,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_NONE
        )
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager =
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(notificationChannel)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
        return notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ola_notification)
            .setContentTitle("Ola Vehicle Server is running")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }

    fun initParserEngine(context: Context) {
        parserEngine.initEngine(
            JsonReaderHelper(
                context,
                R.raw.da_can_dbc
            ).readJsonFromAssetsFolder()
        ) {
            AppLogger.i(TAG, "initialisation of engine done")
        }
    }

    /**
     * @param ipAddress - IP address of connected vehicle network
     * @param portNo - Port no of Grpc server
     */
    fun createChannel(context: Context, ipAddress: String, portNo: Int) {
        if (channel == null) {
            try {
                this.ipAddress = ipAddress
                this.portNo = portNo
                channel = ManagedChannelBuilder.forAddress(ipAddress, portNo).usePlaintext().build()
                sendConnectionBroadcast(context)
                AppLogger.i(TAG, "Grpc channel created")
            } catch (e: Exception) {
                AppLogger.i(TAG, "ManagedChannelBuilder.forAddress ${e.localizedMessage}")
            }
        } else {
            AppLogger.i(TAG, "Grpc channel already created")
        }
    }

    /**
     * This will start streaming continuously
     * where it will use parse parser engine to generate can-data from canId
     */
    @ExperimentalUnsignedTypes
    @Throws(Exception::class)
    fun startDataStreaming() {
        AppLogger.e(TAG, "Data streaming..")
        if (isConnected()) {
            try {
                val wifiCanRpcStub = WifiCanRpcGrpc.newBlockingStub(channel)
                val empty = Empty.newBuilder().build()
                val response = wifiCanRpcStub.canDumpStream(empty)
                response.forEach { canPacket ->
                    val byteArray = canPacket.toByteArray()
                    AppLogger.i(TAG, "Can Frame: " + getCanInfo(byteArray))
                    val canId = ByteHelper.getCanId(byteArray)
                    parserEngine.parseSignal(
                        canId,
                        byteArray.copyOfRange(if (byteArray.size == 18) 10 else 8, byteArray.size)
                    )
                }
            } catch (e: Exception) {
                throw Exception(e.localizedMessage)
            }
        }
    }

    private fun getCanInfo(array: ByteArray): String {
        val iterator = array.iterator()
        val builder = StringBuilder()
        while (iterator.hasNext()) {
            val b = iterator.next()
            builder.append(String.format("%02X", b))
            builder.append(" ")
        }
        println()
        return builder.toString()
    }

    fun getScooterStatus(): ScooterStatus {
        return if (isConnected()) ScooterStatus.CONNECTED else ScooterStatus.DISCONNECTED
    }

    /**
     * param - this is 16 bytes hex string without spaces
     */
    fun operateVehicle(param: String): String {
        try {
            val byteArray = ByteHelper.decodeHexString(param)
            AppLogger.i(TAG, "operateVehicle byteArray $byteArray")
            val byteString = ByteString.copyFrom(byteArray)
            val wifiCanRpcStub = WifiCanRpcGrpc.newBlockingStub(channel)
            val canPacket = CANPacket.newBuilder()
                .setCanFrame(byteString).build()
            val response = wifiCanRpcStub.writeCanFrame(canPacket)
            AppLogger.i(TAG, "response ${response.statusMsg}")
            return response.statusMsg
        } catch (e: Exception) {
            throw Exception(e.localizedMessage)
        }
    }

    fun sendConnectionBroadcast(context: Context) {
        val responseIntent = Intent(ACTION_BROADCAST_SERVICE)
        responseIntent.putExtra(CONNECTED, isConnected())
        context.sendBroadcast(responseIntent)
    }

    private fun isConnected(): Boolean {
        return channel?.isShutdown == false && channel?.isTerminated == false
    }

    fun shutdown() {
        try {
            channel?.shutdownNow()?.awaitTermination(1, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            AppLogger.i(TAG, "${e.message}")
        }
        channel = null
    }
}
