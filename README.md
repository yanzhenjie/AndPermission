# AndPermission
Simplify request run time permission process. AndPermission supports request permissions on Android 5.0 (Api Level 21) and higher, which will return success directly on systems that are lower than Android 5.0.

1. Where to use ?
* With Activity
* With Fragment
* With SupportFragment(In the support library)
* With Context

2. Multiple forms
* Single permission
* Single permission group
* Multiple permissions
* Multiple permission groups

For usage and other information see document: [English](http://yanzhenjie.github.io/AndPermission) | [中文](http://yanzhenjie.github.io/AndPermission/cn).

## Download

* Gradle
```
implementation 'com.yanzhenjie:permission:2.0.0-rc4'
```

* Maven
```
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>permission</artifactId>
  <version>2.0.0-rc4</version>
</dependency>
```

AndPermission requires at minimum Java 7 or Android 4.0(Api level 14) .

## ProGuard
If you are using ProGuard you might need to add the following options:
```
-dontwarn com.yanzhenjie.permission.**
```

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