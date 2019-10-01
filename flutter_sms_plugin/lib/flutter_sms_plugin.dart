import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

class FlutterSmsPlugin {
  static const CHANEL_NAME = "flutter_sms_plugin";
  static final _instance = FlutterSmsPlugin._internal();
  MethodChannel _channel;

  factory FlutterSmsPlugin() => _instance;

  FlutterSmsPlugin._internal(){
    _channel = const MethodChannel(CHANEL_NAME);
    _channel.setMethodCallHandler(handler);
  }




  Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  Future<void> handler(MethodCall call) {
    switch (call.method) {
      case 'readSms':
       // return _selectNotificationCallback(call.arguments);
        return Future.error('method not defined');
      default:
        return Future.error('method not defined');
    }
  }
}
