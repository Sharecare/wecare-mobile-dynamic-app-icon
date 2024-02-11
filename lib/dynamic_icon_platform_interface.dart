import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'dynamic_icon_method_channel.dart';

abstract class DynamicIconPlatform extends PlatformInterface {
  /// Constructs a DynamicIconPlatform.
  DynamicIconPlatform() : super(token: _token);

  static final Object _token = Object();

  static DynamicIconPlatform _instance = MethodChannelDynamicIcon();

  /// The default instance of [DynamicIconPlatform] to use.
  ///
  /// Defaults to [MethodChannelDynamicIcon].
  static DynamicIconPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [DynamicIconPlatform] when
  /// they register themselves.
  static set instance(DynamicIconPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<bool?> setupAppIcon(String iconName) {
    throw UnimplementedError('setupAppIcon() has not been implemented.');
  }
}
