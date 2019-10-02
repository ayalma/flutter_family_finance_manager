package com.ayalma.flutter_sms_plugin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

class SmsBroadcastReceiver(private val channel: MethodChannel) : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {

        channel.invokeMethod("readSms", "test");
    }
}