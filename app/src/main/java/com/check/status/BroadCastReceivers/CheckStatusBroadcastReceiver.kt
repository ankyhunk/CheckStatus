package com.check.status.BroadCastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.check.status.checkWiFiStatus
import com.check.status.sendNotification

private const val TAG = "BroadcastReceiver"

class CheckStatusBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d(TAG, "onReceive")

        val wifiStatus = when (checkWiFiStatus(context)) {
            true -> "WiFi: ON"
            else -> "WiFi: OFF"
        }
        val bluetoothStatus = when (checkWiFiStatus(context)) {
            true -> "Bluetooth: ON"
            else -> "Bluetooth: OFF"
        }

        StringBuilder().apply {
            append(wifiStatus)
            append("\n")
            append("       ")
            //append(bluetoothStatus)
            toString().also { message ->
                Log.d(TAG, message)
                when (intent!!.action) {
                    "android.net.wifi.WIFI_STATE_CHANGED" -> {
                        sendNotification(message, context)
                    }
                }
            }
        }
    }


}