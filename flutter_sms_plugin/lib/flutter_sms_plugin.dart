import 'dart:async';
import 'dart:io';
import 'dart:ui';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_sms_plugin/src/constant.dart';
import 'package:flutter_sms_plugin/src/sms_callback_dispatcher.dart';

// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

typedef Future<dynamic> SmsMessageHandler(String message);

class FlutterSmsPlugin {
  factory FlutterSmsPlugin() => _instance;

  @visibleForTesting
  FlutterSmsPlugin.private(MethodChannel channel) : _channel = channel;

  static final FlutterSmsPlugin _instance = FlutterSmsPlugin.private(
    const MethodChannel(CHANEL_NAME),
  );

  final MethodChannel _channel;
  SmsMessageHandler _smsMessageHandler;

  //final Platform _platform;

  Future<bool>  start() async {


    final CallbackHandle backgroundSetupHandle  =
        PluginUtilities.getCallbackHandle(smsCallbackDispatcher);

    return await _channel.invokeMethod<bool>(
        'SmsPlugin.start', <dynamic>[backgroundSetupHandle.toRawHandle()]);
  }
  Future<bool> config(SmsMessageHandler smsMessageHandler) async
  {
    _smsMessageHandler = smsMessageHandler;

    final CallbackHandle backgroundSetupHandle  =
    PluginUtilities.getCallbackHandle(_smsMessageHandler);
    return await _channel.invokeMethod<bool>(
        'SmsPlugin.config', <dynamic>[backgroundSetupHandle.toRawHandle()]);
  }

  Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  Future<dynamic> _handleMethod(MethodCall call) {
    switch (call.method) {
      case 'readSms':
      //return _callback(call.arguments);
      default:
        return Future.error('method not defined');
    }
  }




}
