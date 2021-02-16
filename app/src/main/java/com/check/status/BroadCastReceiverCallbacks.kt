package com.check.status

interface BroadCastReceiverCallbacks {
    fun wifiStatusChange(message: String)
    fun bluetoothStatusChange(message: String)
}