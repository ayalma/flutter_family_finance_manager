package com.ayalma.flutter_sms_plugin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.view.FlutterNativeView
import io.flutter.view.FlutterView


class FlutterSmsPlugin(private val registrar: Registrar) : MethodCallHandler, PluginRegistry.RequestPermissionsResultListener, PluginRegistry.ViewDestroyListener {

    private val context: Context = registrar.context()
    private val channel: MethodChannel = MethodChannel(registrar.messenger(), CHANEL_NAME)
    private val backgroundChannel: MethodChannel = MethodChannel(registrar.messenger(), BACKGROUND_CHANEL_NAME)
    private val permissions: Array<String> = arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)
    private val TAG = "GeofencingPlugin"


    init {
        channel.setMethodCallHandler(this)
        backgroundChannel.setMethodCallHandler(this)

        SmsService.setBackgroundChannel(backgroundChannel)
        registrar.addRequestPermissionsResultListener(this)
        registrar.addViewDestroyListener(this);
    }

    companion object {
        private const val CHANEL_NAME = "plugins.flutter.io/sms_plugin"
        private const val BACKGROUND_CHANEL_NAME = "plugins.flutter.io/sms_plugin_background"
        private const val PERMISSION_REQUEST_CODE = 10001
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val smsPlugin = FlutterSmsPlugin(registrar)
        }

    }

    override fun onMethodCall(call: MethodCall, result: Result) {

        when (call.method) {
            "SmsPlugin.requestPermissions"->{
                requestPermission()
                result.success(null)

            }
            "SmsPlugin.start" -> {
                val args = call.arguments() as ArrayList<Long>
                var callbackDispatcher: Long = 0
                try {
                    callbackDispatcher = args[0]
                } catch (e: Exception) {
                    Log.e(TAG, "There was an exception when getting callback handle from Dart side")
                    e.printStackTrace()
                }
                SmsService.setCallbackDispatcher(context, callbackDispatcher)
                SmsService.startBackgroundIsolate(context, callbackDispatcher)

                result.success(true)
            }
            "SmsPlugin.initialized" -> {
                SmsService.onInitialized(context)
                result.success(true)
            }
            "SmsPlugin.config" -> {
                val args = call.arguments() as ArrayList<Long>
                var backgroundCallbackHandle: Long = 0
                try {
                    backgroundCallbackHandle = args[0]
                } catch (e: Exception) {
                    Log.e(TAG, "There was an exception when getting callback handle from Dart side")
                    e.printStackTrace()
                }
                SmsService.setBackgroundMessageHandle(context, backgroundCallbackHandle)
            }
            "SmsPlugin.getPlatform" -> {
                // Create Inbox box URI
                val inboxURI = Uri.parse("content://sms/inbox")

// List required columns
                val reqCols = arrayOf("_id", "address", "body")

// Get Content Resolver object, which will deal with Content Provider
                val cr = context.contentResolver

// Fetch Inbox SMS Message from Built-in Content Provider
                val c = cr.query(inboxURI, reqCols, null, null, null)
                var ids = arrayListOf<Long>()
                c?.let { cursor ->
                    if (cursor.moveToFirst()) {
                        do {
                            ids.add(cursor.getLong(cursor.getColumnIndexOrThrow("_id")))
                        } while (cursor.moveToNext())

                    }
                }


                c?.close()
                result.success(ids)
            }
            else -> result.notImplemented()
        }
    }

    /**
     * Transitions the Flutter execution context that owns this plugin from foreground execution to
     * background execution.
     *
     *
     * Invoked when the [FlutterView] connected to the given [FlutterNativeView] is
     * destroyed.
     *
     *
     * Returns true if the given `nativeView` was successfully stored by this plugin, or
     * false if a different [FlutterNativeView] was already registered with this plugin.
     */
    override fun onViewDestroy(nativeView: FlutterNativeView): Boolean {
        return SmsService.setBackgroundFlutterView(nativeView)
    }

    private fun requestPermission() {
        var smsPermision = ContextCompat.checkSelfPermission(context.applicationContext, Manifest.permission.READ_SMS)
        if (smsPermision
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(registrar.activity(),
                            Manifest.permission.READ_SMS)) {
                // ask it with flutter
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(registrar.activity(), permissions, PERMISSION_REQUEST_CODE)

            } else {
                /// no explanation needed just ask permission
                ActivityCompat.requestPermissions(registrar.activity(), permissions, PERMISSION_REQUEST_CODE)
            }

        } else {
            channel.invokeMethod("SmsPlugin.handlePermission",true)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?): Boolean {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> grantResults?.let { results ->
                return if (results.size == 2 && results.all { it == PackageManager.PERMISSION_GRANTED }) {
                    channel.invokeMethod("SmsPlugin.handlePermission",true)
                    false
                } else{
                    channel.invokeMethod("SmsPlugin.handlePermission",false)
                    true
                }
            }
        }
        return true
    }


}
