import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_sms_plugin/flutter_sms_plugin.dart';

void main() {
  const MethodChannel channel = MethodChannel('flutter_sms_plugin');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await FlutterSmsPlugin().platformVersion, '42');
  });
}
