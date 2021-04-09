package com.example.engine_group_isolate_test

import io.flutter.embedding.engine.FlutterEngineGroup
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.engine.FlutterEngineCache
import android.content.Intent
import io.flutter.embedding.engine.FlutterEngine
import android.content.Context
import android.os.Bundle


class MainActivity: FlutterActivity() {
    
    private val engineBinding: MainEngineBinding by lazy {
        MainEngineBinding(activity = this, entrypoint = "main")
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        engineBinding.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        engineBinding.detach()
    }

    override fun provideFlutterEngine(context: Context): FlutterEngine? {
        return engineBinding.engine
    }

    // override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        
    // }

}
