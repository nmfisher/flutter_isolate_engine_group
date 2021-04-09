import Flutter
import UIKit

public class SwiftEngineGroupIsolateTestPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "engine_group_isolate_test", binaryMessenger: registrar.messenger())
    let instance = SwiftEngineGroupIsolateTestPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
