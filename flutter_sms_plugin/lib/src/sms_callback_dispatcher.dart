
import 'dart:ui';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

import 'constant.dart';


/// Setup method channel to handle Sms received while
/// the Flutter app is not active. The handle for this method is generated
/// and passed to the Android side so that the background isolate knows where
/// to send background messages for processing.
///
/// Your app should never call this method directly, this is only for use
/// by the flutter_sms plugin to setup background message handling.
void smsCallbackDispatcher({MethodChannel backgroundChanel = const MethodChannel(BACKGROUND_CHANEL_NAME)}){
  // Setup internal state needed for MethodChannels.
  WidgetsFlutterBinding.ensureInitialized();
  //Listen for background events from the platform portion of the plugin.
  backgroundChanel.setMethodCallHandler((MethodCall call)async{

    final CallbackHandle handle  = CallbackHandle.fromRawHandle(call.arguments["handle"]);
    final Function handlerFunction = PluginUtilities.getCallbackFromHandle(handle);
    try{
      await handlerFunction(call.arguments["message"].toString());
    }catch(e)
    {
      print('Unable to handle incoming background message.');
      print(e);
    }
    return Future<void>.value();
  });
  // Once we've finished initializing, let the native portion of the plugin
  // know that it can start scheduling handling messages.
  backgroundChanel.invokeMethod<void>('SmsPlugin.initialized');
}
