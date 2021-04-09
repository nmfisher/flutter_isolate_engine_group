#import "EngineGroupIsolateTestPlugin.h"
#if __has_include(<engine_group_isolate_test/engine_group_isolate_test-Swift.h>)
#import <engine_group_isolate_test/engine_group_isolate_test-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "engine_group_isolate_test-Swift.h"
#endif

@implementation EngineGroupIsolateTestPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftEngineGroupIsolateTestPlugin registerWithRegistrar:registrar];
}
@end
