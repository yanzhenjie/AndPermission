# AndPermission
* Requesting runtime permissions on Android 6.0 or higher.
* Sharing private files on Android 7.0 or higher.
* Installing unknown source apk on Android 8.0 or higher.  
  android.permission.REQUEST_INSTALL_PACKAGES
* Drawing at the top of other apps on Android 6.0 or higher.  
  android.permission.SYSTEM_ALERT_WINDOW.

```java
AndPermission.with(this)
  .runtime()
  .permission(Permission.Group.STORAGE)
  .onGranted(permissions -> {
    // Storage permission are allowed.
  })
  .onDenied(permissions -> {
    // Storage permission are not allowed.
  })
  .start();
```

```java
File file = ...;
Uri compatUri = AndPermission.getFileUri(this, file);
```

```java
File apkFile = ...;

AndPermission.with(this)
  .install()
  .file(apkFile)
  .onGranted(file -> {
    // App is allowed to install apps.
  })
  .onDenied(file -> {
    // App is refused to install apps.
  })
  .start();
```

```java
AndPermission.with(this)
  .overlay()
  .onGranted(data -> {
    // App can draw on top of other apps.
  })
  .onDenied(data -> {
    // App cann't draw on top of other apps.
  })
  .start();
```

For documentation and additional information see [the website](http://yanzhenjie.github.io/AndPermission).

## Download
```
implementation 'com.yanzhenjie:permission:2.0.0-rc12'
```
AndPermission requires at minimum Android 4.0(Api level 14) .

## ProGuard
If you are using ProGuard you might need to add the following options:
```
-dontwarn com.yanzhenjie.permission.**
```

## Contributing
Before submitting pull requests, contributors must abide by the [agreement](CONTRIBUTING.md) .

## License
```text
Copyright 2018 Yan Zhenjie

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```