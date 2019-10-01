package com.ayalma.flutter_sms_plugin

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.plugin.common.PluginRegistry

class FlutterSmsPlugin(registrar: Registrar) : MethodCallHandler, BroadcastReceiver(), PluginRegistry.RequestPermissionsResultListener {
    var activity: Activity = registrar.activity()
    var channel = MethodChannel(registrar.messenger(), CHANEL_NAME)
    val permissions = arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)

    init {
        channel.setMethodCallHandler(this)
        registrar.addRequestPermissionsResultListener(this)
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        channel.invokeMethod("readSms", "test");
    }

    companion object {
        private const val CHANEL_NAME = "flutter_sms_plugin"
        private const val PERMISSION_REQUEST_CODE = 10001
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val smsPlugin = FlutterSmsPlugin(registrar)
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
        var smsPermision =  ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS)
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
