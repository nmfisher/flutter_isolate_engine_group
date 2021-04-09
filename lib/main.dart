import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_isolate/flutter_isolate.dart';
import 'flutter_engine_group_isolate.dart' as f2;

void main() {
  runApp(MyApp());
}

void spawn_with_engine_group(int ms) async {
  print("Spawned from FlutterEngineGroup in ${DateTime.now().millisecondsSinceEpoch - ms}ms");
}

void spawn_with_flutter_isolate(int ms) {
  print("Spawned from flutter_isolate in ${DateTime.now().millisecondsSinceEpoch - ms}ms");
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
              onPressed: () async {
                await f2.FlutterIsolate.spawn<int>(spawn_with_engine_group, DateTime.now().millisecondsSinceEpoch);
              },
              child: Text("Spawn with Flutter Engine Group"),
            ),
            ElevatedButton(
              onPressed: () async {
                await FlutterIsolate.spawn<int>(
                    spawn_with_flutter_isolate, DateTime.now().millisecondsSinceEpoch);
              },
              child: Text("Spawn with flutter_isolate"),
            ),
          ],
        ),
      ),
    );
  }
}
