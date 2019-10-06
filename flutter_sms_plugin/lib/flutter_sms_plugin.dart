import 'dart:async';
import 'dart:ui';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_sms_plugin/src/constant.dart';
import 'package:flutter_sms_plugin/src/sms_callback_dispatcher.dart';

// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

typedef Future<dynamic> SmsMessageHandler(String message);
typedef Future<dynamic> PermissionCallbackHandler(bool granted);

class FlutterSmsPlugin {
  factory FlutterSmsPlugin() => _instance;

  @visibleForTesting
  FlutterSmsPlugin.private(MethodChannel channel) {
    _channel = channel;
    _channel.setMethodCallHandler(_handleMethod);
  }

  static final FlutterSmsPlugin _instance = FlutterSmsPlugin.private(
    const MethodChannel(CHANEL_NAME),
  );

  MethodChannel _channel;
  SmsMessageHandler _smsMessageHandler;

  Future<List<dynamic>> platformVersion() async {
    return await _channel.invokeMethod<List<dynamic>>('SmsPlugin.getPlatform');
  }

  PermissionCallbackHandler _permissionCallbackHandler;

  Future<void> requestPermissions(
      PermissionCallbackHandler permissionCallbackHandler) async {
    this._permissionCallbackHandler = permissionCallbackHandler;
    return await _channel.invokeMethod<void>('SmsPlugin.requestPermissions');
  }

  Future<void> readSms(){

  }

  Future<bool> start() async {
    final CallbackHandle backgroundSetupHandle =
        PluginUtilities.getCallbackHandle(smsCallbackDispatcher);

    return await _channel.invokeMethod<bool>(
        'SmsPlugin.start', <dynamic>[backgroundSetupHandle.toRawHandle()]);
  }

  Future<bool> config(SmsMessageHandler smsMessageHandler) async {
    _smsMessageHandler = smsMessageHandler;

    final CallbackHandle backgroundSetupHandle =
        PluginUtilities.getCallbackHandle(_smsMessageHandler);
    return await _channel.invokeMethod<bool>(
        'SmsPlugin.config', <dynamic>[backgroundSetupHandle.toRawHandle()]);
  }

  Future<dynamic> _handleMethod(MethodCall call) {
    switch (call.method) {
      case 'SmsPlugin.handlePermission':
        if (_permissionCallbackHandler != null) {
          return _permissionCallbackHandler(call.arguments);
        }
        return Future.error('method not defined');
      default:
        return Future.error('method not defined');
    }
  }
}
