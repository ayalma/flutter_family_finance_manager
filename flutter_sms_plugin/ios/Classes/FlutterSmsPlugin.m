#import "FlutterSmsPlugin.h"
#import <flutter_sms_plugin/flutter_sms_plugin-Swift.h>

@implementation FlutterSmsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterSmsPlugin registerWithRegistrar:registrar];
}
@end
