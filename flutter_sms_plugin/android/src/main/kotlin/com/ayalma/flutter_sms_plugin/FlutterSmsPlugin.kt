package com.ayalma.flutter_sms_plugin

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

class FlutterSmsPlugin(registrar: Registrar) : MethodCallHandler, BroadcastReceiver() {
  var activity: Activity = registrar.activity()
  var channel =  MethodChannel(registrar.messenger(), CHANEL_NAME)


  override fun onReceive(p0: Context?, p1: Intent?) {
    channel.invokeMethod()
  }

  companion object {
    private const val CHANEL_NAME = "flutter_sms_plugin"
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val smsPlugin = FlutterSmsPlugin(registrar)
    }
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else {
      result.notImplemented()
    }
  }
}
