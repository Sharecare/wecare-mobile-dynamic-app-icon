package com.hishamreffat.dynamic_app_icon
import android.app.Activity
import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.*
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.startActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.lang.StringBuilder
import android.content.Context
import java.lang.ref.WeakReference
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

/** DynamicAppIconPlugin */
class DynamicAppIconPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    var iconList: List<String> = emptyList()
    var appContext: Context? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        appContext = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "dynamic_app_icon")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "setupAppIcon") {
            val iconName = call.argument<String>("iconName") ?: ""
            val packageName: String = call.argument<String?>("packageName") ?: ""
            print(packageName)
            val isSuccess = setupIcon(iconName, packageName)
            result.success(isSuccess)
        } else if (call.method == "setupIconList") {
            iconList = call.arguments as List<String>
            result.success("success")
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private fun setupIcon(iconName: String, packageName: String): Boolean {
        val context = appContext ?: return false
        for (name in iconList) {
            var cName = name
            if (name == "default") {
                cName = "MainActivity"
            }
            val computedPackageName = if (packageName == "") context.packageName else packageName
            val componentName = ComponentName(context, computedPackageName + ".$cName")
            val enable = name == iconName
            updateAlias(enable, componentName)
        }
        return true;
    }


    private fun updateAlias(enable: Boolean, componentName: ComponentName) {
        val context = appContext ?: return
        val newState = if (enable) {
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        } else {
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        }
        context.packageManager.setComponentEnabledSetting(componentName, newState, PackageManager.DONT_KILL_APP)
    }
}