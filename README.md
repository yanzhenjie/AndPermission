# AndPermission
Simplify the process of requesting permission on Android.
* Request runtime permissions on Android 6.0 or higher.
* Share private files on Android 7.0 or higher.
* Install the app on Android 8.0 or higher.

```java
AndPermission.with(this)
  .runtime()
  .permission(Permission.Group.STORAGE)
  .onGranted(permissions -> {
    // TODO ...
  })
  .onDenied(permissions -> {
    // TODO ...
  })
  .start();
```

For documentation and additional information see [the website](http://yanzhenjie.github.io/AndPermission).

## Download
```
implementation 'com.yanzhenjie:permission:2.0.0-rc5'
```
AndPermission requires at minimum Java 7 or Android 4.0(Api level 14) .

## Plan
Request `SYSTEM_ALERT_WINDOW` permission at runtime, you can track [the progress of task](https://github.com/yanzhenjie/AndPermission/projects).

## ProGuard
If you are using ProGuard you might need to add the following options:
```
-dontwarn com.yanzhenjie.permission.**
```

## Contributing
Before submitting pull requests, contributors must abide by the [agreement](CONTRIBUTING.md) .

## License
```text
Copyright 2016 Yan Zhenjie

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