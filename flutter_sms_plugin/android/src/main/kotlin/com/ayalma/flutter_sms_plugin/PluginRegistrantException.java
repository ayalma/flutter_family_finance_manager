package com.ayalma.flutter_sms_plugin;

class PluginRegistrantException extends RuntimeException {
    public PluginRegistrantException() {
        super(
                "PluginRegistrantCallback is not set. Did you forget to call "
                        + "AlarmService.setPluginRegistrant? See the README for instructions.");
    }
}