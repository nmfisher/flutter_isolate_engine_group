# engine_group_isolate_test

This is a reimplementation of the [flutter_isolate](https://github.com/rmawatson/flutter_isolate) Flutter plugin, using FlutterEngineGroup to spawn new isolates.

This is intended to take advantage of the new "lightweight" FlutterEngineGroup implementation that shares resources between multiple FlutterEngines. The hope is that this will halve the overhead of starting a new isolate.

Note that the new FlutterEngineGroup API is not yet available as of Flutter 2.1, so this repository currently offers no advantage over the flutter_isolate plugin.

Once the new API is available, this repository should serve as a basis for a "flutter_isolate 2.0" plugin.

For further details, see https://github.com/flutter/flutter/issues/72009, https://github.com/flutter/flutter/issues/13937#issuecomment-784571153 and https://github.com/flutter/samples/tree/master/add_to_app/multiple_flutters.

Currently only an Android implementation is available. (also there's an issue with threadmerging and PlatformViews (see https://github.com/flutter/flutter/issues/73620) which means this repository is unusable when using the Unity plugin). I'm hoping for a workaround by the time the new FlutterEngineGroup API is available.

## Getting Started

- add this project as plugin in your pubspec.yaml
- change your AndroidManifest.xml <application> to launch com.example.engine_group_isolate_test.App:
```
<application
        android:name="io.flutter.app.FlutterApplication"
        ...
```

and to launch com.example.engine_group_isolate.MainActivity as your activity
```
<activity
            android:name="io.flutter.embedding.android.FlutterActivity"
            ...
```

- invoke FlutterIsolate.spawn() in the same way you would via the flutter_isolate plugin.

See the example project for an actual implementation.

TODO
- is it possible to spawn a Dart isolate without a FlutterEngine? If your spawned isolate is just doing some background work and communicating via platform channels, why do we need another FlutterEngine?