package com.ayalma.flutter_sms_plugin

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.plugin.common.PluginRegistry

class FlutterSmsPlugin(registrar: Registrar) : MethodCallHandler, BroadcastReceiver(),PluginRegistry.RequestPermissionsResultListener {
  var activity: Activity = registrar.activity()
  var channel =  MethodChannel(registrar.messenger(), CHANEL_NAME)

  init {
    channel.setMethodCallHandler(this)
    registrar.addRequestPermissionsResultListener(this)
  }
  override fun onReceive(p0: Context?, p1: Intent?) {
    channel.invokeMethod("readSms","test");


  }

  companion object {
    private const val CHANEL_NAME = "flutter_sms_plugin"
    private const val PERMISSION_REQUEST_CODE = 10001;
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val smsPlugin = FlutterSmsPlugin(registrar)
    }
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "getPlatformVersion") {
      requestPermission()
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else {
      result.notImplemented()
    }
  }

  private fun requestPermission(){
      val permissions = arrayOf(Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS)
      ActivityCompat.requestPermissions(activity,permissions, PERMISSION_REQUEST_CODE)
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?): Boolean {
    if(requestCode == PERMISSION_REQUEST_CODE)
    {
        grantResults?.let { results ->
            if (results.size == 2 && results.all { it == PackageManager.PERMISSION_GRANTED }) {

                return true
            }
        }
    }
    return false
  }


}
