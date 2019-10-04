package com.ayalma.flutter_family_finance_manager

import com.ayalma.flutter_sms_plugin.FlutterSmsPlugin
import com.ayalma.flutter_sms_plugin.SmsService
import io.flutter.app.FlutterApplication
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugins.GeneratedPluginRegistrant

class App : FlutterApplication(), PluginRegistry.PluginRegistrantCallback {
    override fun onCreate() {
        super.onCreate()
        SmsService.setPluginRegistrant(this)
    }
    override fun registerWith(registry: PluginRegistry?) {
        GeneratedPluginRegistrant.registerWith(registry)
    }
}