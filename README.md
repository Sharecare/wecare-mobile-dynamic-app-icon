# dynamic_app_icon_flutter

A flutter plugin for dynamically changing app icon in mobile platform. Supports **iOS and Android
** (IOS with version > `10.3`).

* Due to Splash screen problems of newest Android versions, please consider to remove the below code in your activity and activity-alias tags:

```
  <meta-data
                android:name="io.flutter.embedding.android.SplashScreenDrawable"
                android:resource="@drawable/launch_background"
                />
```

* Add this code to your MainActivity.kt, in onCreate function:

```
	 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Disable the Android splash screen fade out animation to avoid
            // a flicker before the similar frame is drawn in Flutter.
            splashScreen.setOnExitAnimationListener { splashScreenView -> splashScreenView.remove() }
        }
```

### Android  Integration

1. Add the latest version of the plugin to your `pubpsec.yaml` under dependencies section
2. Run `flutter pub get`
3. Update `android/src/main/AndroidManifest.xml` as follows:
    ```xml
    <application ...>

        <activity
            android:name=".MainActivity"
            android:launchMode="singleTop"
            android:theme="@style/LaunchTheme"
            android:configChanges="orientation|keyboardHidden|keyboard|screenSize|smallestScreenSize|locale|layoutDirection|fontScale|screenLayout|density|uiMode"
            android:hardwareAccelerated="true"
            android:windowSoftInputMode="adjustResize"
            android:enabled="true">
				
            <meta-data
                android:name="io.flutter.embedding.android.NormalTheme"
                android:resource="@style/NormalTheme"
                />

            <meta-data
                android:name="io.flutter.embedding.android.SplashScreenDrawable"
                android:resource="@drawable/launch_background"
                />

            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- The activity-alias are your alternatives icons and name of your app, the default one must be enabled (and the others disabled) and the name must be ".DEFAULT". All the names of your activity-alias' name must begin with a dot. -->

        <!-- FOR NOW USE "icon_1" AS ALTERNATIVE ICON NAME -->

        <activity-alias
            android:label="Your app"
            android:icon="@mipmap/ic_launcher_1"
            android:name=".icon_1"
            android:enabled="false"
            android:targetActivity=".MainActivity">

            <meta-data
                android:name="io.flutter.embedding.android.NormalTheme"
                android:resource="@style/NormalTheme"
                />

            <meta-data
                android:name="io.flutter.embedding.android.SplashScreenDrawable"
                android:resource="@drawable/launch_background"
                />

            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>

        </activity-alias>
    </application>
    ```
4. You can have multiple app icon, in your app you can now use:
    * The name you pass in the method must be in the `AndroidManifest.xml` and for each icon, you
      must declare an activity-alias in `AndroidManifest.xml` like above
    * Declare an list of string (your available app icons)
    * Dont forget to add `default` to your list

### Android code Integration

```dart
    const List<String> list = ["icon_1", "default"];
      DynamicAppIcon.setupAppIcon(iconName: 'icon_1', iconList: list);
```      

### iOS Integration

#### Index

* `2x` - `120px x 120px`
* `3x` - `180px x 180px`

To integrate your plugin into the iOS part of your app, follow these steps

1. First let us put a few images for app icons, they are
    * `teamfortress@2x.png`, `teamfortress@3x.png`
    * `photos@2x.png`, `photos@3x.png`,
    * `chills@2x.png`, `chills@3x.png`,
2. These icons shouldn't be kept in `Assets.xcassets` folder, but outside

3. Next, we need to setup the `Info.plist`
    1. Add `Icon files (iOS 5)` to the Information Property List
    2. Add `CFBundleAlternateIcons` as a dictionary, it is used for alternative icons
    3. Set 3 dictionaries under `CFBundleAlternateIcons`, they are correspond
       to `teamfortress`, `photos`, and `chills`
    4. For each dictionary, two properties —`UIPrerenderedIcon` and `CFBundleIconFiles` need to be
       configured

Note that if you need it work for iPads, You need to add these icon declarations
in `CFBundleIcons~ipad` as
well. [See here](https://developer.apple.com/library/archive/documentation/General/Reference/InfoPlistKeyReference/Articles/CoreFoundationKeys.html#//apple_ref/doc/uid/TP40009249-SW14)
for more details.

When prompted with a popup below, select “Create groups”. This step is essential, as if not done correctly, it will result in an error when uploading to the AppStore.

Here is `Info.plist` after adding Alternate Icons

#### Screenshot

![directory_structure](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*bNWtT-NoJzEnGzXONyC8aQ.png)
![info.plist](https://raw.githubusercontent.com/tastelessjolt/flutter_dynamic_icon/master/imgs/info-plist.png)

#### Raw

```
... the rest of Info.plist

<key>CFBundleIcons</key>
<dict>
<!-- Default app icon -->
<key>CFBundlePrimaryIcon</key>
<dict>
   <key>CFBundleIconFiles</key>
   <array>
      <string>icon</string>
   </array>
   <key>UIPrerenderedIcon</key>
   <false/>
</dict>
<key>CFBundleAlternateIcons</key>
<dict>
   <!-- First custom icon - Black -->
   <key>icon</key>
   <dict>
      <key>CFBundleIconFiles</key>
      <array>
         <string>icon</string>
      </array>
      <key>UIPrerenderedIcon</key>
      <false/>
   </dict>
</dict>
</dict>

... the rest of Info.plist
```

Now, you can call `DynamicAppIcon.setAlternateIconName` with the `CFBundleAlternateIcons` key as
the argument to set that icon.

### IOS Code Integration

From your Dart code, you need to import the plugin and use it's static methods:

```dart 
import 'package:dynamic_app_icon/dynamic_app_icon.dart';

try {
if (await DynamicAppIcon.supportsAlternateIcons) {
await DynamicAppIcon.setAlternateIconName("photos");
print("App icon change successful");
return;
}
} on PlatformException
catch
(
e) {
if (await DynamicAppIcon.supportsAlternateIcons) {
await DynamicAppIcon.setAlternateIconName(null);
print("Change app icon back to default");
return;
} else {
print("Failed to change app icon");
}
}

```
