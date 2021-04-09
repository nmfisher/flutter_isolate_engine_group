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

/**
 * FlutterIsolatePlugin
 */

class IsolateHolder(isolateId : String, entryPoint:DartExecutor.DartEntrypoint, result: MethodChannel.Result) {

    lateinit var engine: FlutterEngine

    lateinit var startupChannel:EventChannel
    lateinit var controlChannel:MethodChannel

    val entryPoint:DartExecutor.DartEntrypoint
    val result: MethodChannel.Result
    val isolateId : String
        
    init {
        this.isolateId = isolateId
        this.entryPoint = entryPoint
        this.result = result
    }

}