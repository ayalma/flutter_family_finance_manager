package com.ayalma.flutter_family_finance_manager

import com.ayalma.flutter_sms_plugin.FlutterSmsPlugin
import io.flutter.app.FlutterApplication
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugins.GeneratedPluginRegistrant

class App : FlutterApplication(), PluginRegistry.PluginRegistrantCallback {
    override fun onCreate() {
        super.onCreate()
        FlutterSmsPlugin.setPluginRegistrantCallback(this)
    }
    override fun registerWith(registry: PluginRegistry?) {
        GeneratedPluginRegistrant.registerWith(registry)
    }
}