package com.example.engine_group_isolate_test

import android.app.Activity

import io.flutter.FlutterInjector
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.EventChannel.StreamHandler;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This binds the main FlutterEngine instance to a channel for spawning/killing new engines/isolates.
 *
  *
 * @see main.dart for what messages are getting sent from Flutter.
 */
class MainEngineBinding(activity: Activity, entrypoint: String) : MethodCallHandler, StreamHandler {
    
    val NAMESPACE : String = "com.avinium.engine_isolate";
    lateinit var channel: MethodChannel
    lateinit var engine: FlutterEngine
    val app : App
    val activity : Activity

    val queuedIsolates:Queue<IsolateHolder> = LinkedList<IsolateHolder>();
    val activeIsolates:HashMap<String, IsolateHolder> = HashMap<String, IsolateHolder>()

    init {
        this.activity = activity
        app = activity.applicationContext as App
        val dartEntrypoint =
            DartExecutor.DartEntrypoint(
                FlutterInjector.instance().flutterLoader().findAppBundlePath(), entrypoint
            )
        engine = app.engines.createAndRunEngine(activity, dartEntrypoint)
        channel = MethodChannel(engine.dartExecutor.binaryMessenger, NAMESPACE + "/control")
        android.util.Log.e("FlutterIsolate", "Created channel; with " + NAMESPACE + "/control")
    }

    /**
     * This setups the messaging connections on the platform channel.
     */
    fun attach() {
        channel.setMethodCallHandler(this)
        android.util.Log.e("FlutterIsolate", "Set method call handler");
    }

    fun queueIsolate(entryPoint:String, entryPointFilename:String, isolateId:String, result:MethodChannel.Result) {
        android.util.Log.e("FlutterIsolate", "Queueing isolate")

        val dartEntrypoint = DartExecutor.DartEntrypoint(
            FlutterInjector.instance().flutterLoader().findAppBundlePath(), entryPointFilename, entryPoint
        )

        val isolate = IsolateHolder(isolateId, dartEntrypoint, result)

        queuedIsolates.add(isolate);

        if (queuedIsolates.size == 1) 
            startNextIsolate();
    
    }

    fun startNextIsolate() {
        val isolate = queuedIsolates.peek()
        isolate.engine = app.engines.createAndRunEngine(activity, isolate.entryPoint)
        isolate.controlChannel = MethodChannel(isolate.engine.getDartExecutor().getBinaryMessenger(), NAMESPACE + "/control");
        isolate.startupChannel = EventChannel(isolate.engine.getDartExecutor().getBinaryMessenger(), NAMESPACE + "/event");
        isolate.startupChannel.setStreamHandler(this);
        isolate.controlChannel.setMethodCallHandler(this);
    }

    fun killEngine(isolateId:String, result:MethodChannel.Result) {
        val isolate = activeIsolates.get(isolateId)!!
        isolate.engine.destroy()
        activeIsolates.remove(isolateId)
        result.success(null)
    }

    override fun onListen(o:Any?, sink:EventChannel.EventSink) {
        if (queuedIsolates.size == 0)
            return;
        val isolate = queuedIsolates.remove();
        sink.success(isolate.isolateId);
        sink.endOfStream();
        activeIsolates.put(isolate.isolateId, isolate);
        isolate.result.success(null);

        if (queuedIsolates.size != 0)
            startNextIsolate();
    }

    override fun onMethodCall(call:MethodCall, result:MethodChannel.Result) {
        android.util.Log.e("FlutterIsolate", "Got method call:" + call.method)

        when (call.method) {
            "spawn_isolate" -> {                  
                queueIsolate(call.argument("entry_point")!!, call.argument("entry_point_filename")!!, call.argument("isolate_id")!!, result) 
            }
            "kill_isolate" -> {
                killEngine(call.argument("isolate_id")!!, result)
                result.success(null)
            }   
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun onCancel(o:Any?) {
        android.util.Log.e("FlutterIsolate", "Stream cancelled.");
    }

    /**
     * This tears down the messaging connections on the platform channel.
     */
    fun detach() {
        // engine.destroy();
    }

}