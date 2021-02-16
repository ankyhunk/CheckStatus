package com.check.status

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.check.status.BroadCastReceivers.CheckStatusBroadcastReceiver
import com.check.status.Services.WifiStatusCheckService

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

private const val TAG = "FirstFragment"

class FirstFragment : Fragment() {

    private lateinit var wifiStatusBroadcastReceiver: CheckStatusBroadcastReceiver
    private val WIFI_REQUEST_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        // UI Components
        val wifiSwitch = view.findViewById<Switch>(R.id.switch_network_status)
        val bluetoothSwitch = view.findViewById<Switch>(R.id.switch_bluetooth_status)
        val serviceSwitch = view.findViewById<Switch>(R.id.switch_service)

        //Create Notification channel
        createNotificationChannel(context)

        wifiStatusBroadcastReceiver = CheckStatusBroadcastReceiver()

        // Initialize the service
        val service = Intent(context, WifiStatusCheckService::class.java)

        // Set the listener to the Service switch to set Service ON & OFF
        serviceSwitch.apply {
            isChecked = context?.startService(service) != null
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    val service = Intent(context, WifiStatusCheckService::class.java)
                    context?.startService(service)
                } else {
                    val service = Intent(context, WifiStatusCheckService::class.java)
                    context?.stopService(service)
                }
            }
        }

        wifiSwitch.apply {
            //Set the Wifi Switch state
            isChecked = checkWiFiStatus(context)
            setOnClickListener {
                setWiFiStatus(false, WIFI_REQUEST_CODE)
            }
        }

        return view
    }
    

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            WIFI_REQUEST_CODE -> {
                //Check for the Wifi Status
                view?.findViewById<Switch>(R.id.switch_network_status)?.isChecked =
                    checkWiFiStatus(context)
            }
        }
    }

}