package com.ayalma.flutter_sms_plugin

import android.Manifest
import android.app.Activity
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.provider.Telephony
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar

class FlutterSmsPlugin() : MethodCallHandler, PluginRegistry.RequestPermissionsResultListener {

    constructor(registrar: Registrar) : this() {
        this.activity = registrar.activity()
        this.channel = MethodChannel(registrar.messenger(), CHANEL_NAME)
        this.permissions = arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)
        channel.setMethodCallHandler(this)
        registrar.addRequestPermissionsResultListener(this)
        registrar.activeContext().registerReceiver(SmsBroadcastReceiver(channel), IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
    }

    lateinit var activity: Activity
    lateinit var channel: MethodChannel
    lateinit var permissions: Array<String>


    companion object {
        private const val CHANEL_NAME = "flutter_sms_plugin"
        private const val PERMISSION_REQUEST_CODE = 10001
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val smsPlugin = FlutterSmsPlugin(registrar)
        }

        fun setPluginRegistrantCallback(app: PluginRegistry.PluginRegistrantCallback) {
         //   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "getPlatformVersion") {
            requestPermission {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            }
        } else {
            result.notImplemented()
        }
    }

    private fun requestPermission(okCallback: () -> Unit) {
        var smsPermision = ContextCompat.checkSelfPermission(activity.applicationContext, Manifest.permission.READ_SMS)
        if (smsPermision
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.READ_SMS)) {
                // ask it with flutter
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE)

            } else {
                /// no explanation needed just ask permission
                ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE)
            }

        } else {
            okCallback.invoke()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?): Boolean {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> grantResults?.let { results ->
                if (results.size == 2 && results.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // call method that nee
                    return false
                }
            }
        }
        return true
    }

}
