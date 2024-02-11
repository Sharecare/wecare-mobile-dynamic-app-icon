import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'dynamic_icon_platform_interface.dart';

/// An implementation of [DynamicIconPlatform] that uses method channels.
class MethodChannelDynamicIcon extends DynamicIconPlatform {
  List<String> iconList = [];

  @visibleForTesting
  final methodChannel = const MethodChannel('dynamic_icon');

  @override
  Future<bool?> setupAppIcon(String iconName) async {
    if (!iconList.contains(iconName)) return false;
    await methodChannel.invokeMethod("setupIconList", iconList);
    final result =
        await methodChannel.invokeMethod<bool>('setupAppIcon', iconName);
    return result;
  }
}
