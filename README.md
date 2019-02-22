# AndPermission
1. Request for runtime permissions.  
2. Share private files.  
3. Request to install unknown source apk.  
  `android.permission.REQUEST_INSTALL_PACKAGES`
4. Request to draw at the top of other apps.  
  `android.permission.SYSTEM_ALERT_WINDOW`
5. Request to show notifications.  
6. Request to access notifications.  
  `android.permission.BIND_NOTIFICATION_LISTENER_SERVICE`

For documentation and additional information see [the website](https://www.yanzhenjie.com/AndPermission).

**1. Request for runtime permissions**
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

**2. Sharing private files**
```java
File file = ...;
Uri compatUri = AndPermission.getFileUri(this, file);
...
```

**3. Request to install unknown source apk**
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

**4. Request to draw at the top of other apps**
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

**5. Request to send notifications**
```java
AndPermission.with(this)
  .notification()
  .permission()
  .onGranted(data -> {
    // App can show notifications.
  })
  .onDenied(data -> {
    // App cann't show notifications.
  })
  .start();
```

**6. Request to access notifications**
```java
AndPermission.with(this)
  .notification()
  .listener()
  .onGranted(data -> {
    // App can access notifications.
  })
  .onDenied(data -> {
    // App cann't access notifications.
  })
  .start();
```

## Download
If you are using the android support library:
```
implementation 'com.yanzhenjie.permission:support:2.0.0'
```

If you are using the android x library:
```
implementation 'com.yanzhenjie.permission:x:2.0.0'
```

AndPermission requires at minimum Android 4.0(Api level 14) .

## Contributing
Before submitting pull requests, contributors must abide by the [agreement](CONTRIBUTING.md) .

## License
```text
Copyright 2019 Zhenjie Yan

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