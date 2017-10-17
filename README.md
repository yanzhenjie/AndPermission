Request for permission anywhere, and compatible with Android O.

[Why is it compatible with Android O ?](https://developer.android.com/preview/behavior-changes.html#rmp)

[中文文档](./README-CN.md)  
只针对中国开发者：[国产机兼容方案](https://github.com/yanzhenjie/AndPermission/blob/master/README-CN.md#国产手机适配方案)  

----
# Features
1. Chain call, a word to request for permissions.
2. Support annotation callback results, support Listener callback results.
3. `Rationale`: When the user refuse a permission, you request this permission again, show a description of the application's permission to the user, with the consent of the user to continue to request, avoid the user check `[Never ask again]`.
4. Even if the user refused permission and check `[Never ask again]`, you can use `SettingDialog` to allows the user to open your APP's `Setting Page` authorization.
5. `RationaleDialog`and`SettingDialog` allow developers to customize.
7. Support the right to apply for permission at any place, not limited to `Activity` and` Fragment`.

# Dependencies
* Gradle
```groovy
compile 'com.yanzhenjie:permission:1.1.2'
```

* Maven
```xml
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>permission</artifactId>
  <version>1.1.2</version>
  <type>pom</type>
</dependency>
```

# Usage
It is recommended to download Demo and read README, which can help you understand.

## Request Permission
**Attention:** You do not have to make any judgments before request permissions, `AndPermission` will automatically judge, no permissions will be requested.

```java
// Activity:
AndPermission.with(activity)
    .requestCode(100)
    .permission(Permission.SMS)
    .rationale(...)
    .callback(...)
    .start();

// Fragment:
AndPermission.with(fragment)
    .requestCode(100)
    .permission(
        // Multiple permissions group.
        Permission.SMS,
        Permission.LOCATION
    )
    .rationale(...)
    .callback(...)
    .start();

// Anywhere:
AndPermission.with(context)
    .requestCode(100)
    .permission(Permission.SMS)
    .rationale(...)
    .callback(...)
    .start();

// If only want to request a permission: 
AndPermission.with(this)
    .requestCode(300)
    .permission(Manifest.permission.WRITE_CONTACTS)
    .rationale(...)
    .callback(...)
    .start();

// If only want to request some permissions: 
AndPermission.with(this)
    .requestCode(300)
    .permission(
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.READ_SMS
    )
    .rationale(...)
    .callback(...)
    .start();
```


## Callback results
Received callback results in two ways: The `Listener` and `Annotation Method`.

### Listener
In the `callback()` method passed `PermissionListener`.
```java
AndPermission.with(context)
    ...
    .requestCode(200)
    .callback(listener)
    .start();

private PermissionListener listener = new PermissionListener() {
    @Override
    public void onSucceed(int requestCode, List<String> grantedPermissions) {
        // Successfully.
        if(requestCode == 200) {
            // TODO ...
        }
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        // Failure.
        if(requestCode == 200) {
            // TODO ...
        }
    }
};
```

### Annotation
In the `callback()` method, you can pass the instance of your callback method.
```java
AndPermission.with(context)
    ...
    .requestCode(300)
    .callback(this)
    .start();

// The 300 is the the requestCode().
@PermissionYes(300)
private void getPermissionYes(List<String> grantedPermissions) {
    // Successfully.
}

@PermissionNo(300)
private void getPermissionNo(List<String> deniedPermissions) {
    // Failure.
}
```

## Rationale
`Runtime permissions` have a feature, after refused a permission, re-request for the permission, system will be more than a `[Never ask again]` check box in the dialog, when the user check the `[Never ask again]` and refused permission, the next time to request permission, the system will callback failed.  
So the `Rationale` function is in the user refused a permission, when you apply for permission again, you will see a description, after the user allows permission to apply for permission, this prevents the user from checking for `[Never ask again]`.

### Method 1: The default MD style dialog
```java
AndPermission.with(this)
    ...
    .requestCode(...)
    .rationale((requestCode, rationale) ->
        AndPermission.rationaleDialog(context, rationale).show()
    )
    .start()
```

### Method 2: Customize the dialog
```java
AndPermission.with(this)
    ...
    .requestCode(...)
    .rationale(rationaleListener)
    .start()

private RationaleListener rationaleListener = (requestCode, rationale) -> {
    AlertDialog.newBuilder(this)
        .setTitle("Tips")
        .setMessage("Request permission to recommend content for you.")
        .setPositiveButton("OK", (dialog, which) -> {
            rationale.resume();
        })
        .setNegativeButton("NO", (dialog, which) -> {
            rationale.cancel();
        }).show();
};
```

## SettingDialog
When the user checked `[Never ask again]`, we can prompt the user to open your APP's `Setting Page` authorization.

We add the following code in the failure callback method, the following three options can be:
```java
// To judge whether to check the [Never ask again].
if (AndPermission.hasAlwaysDeniedPermission(activity, deniedPermissions)) {
    // First type: with AndPermission default prompt.
    AndPermission.defaultSettingDialog(activity, 400).show();

    // Second: with a custom prompt.
    AndPermission.defaultSettingDialog(activity, 400)
    .setTitle("Permission Failure")
    .setMessage("Have you denied some of the necessary permissions,"
        + " the operation can not continue, is it reauthorized?")
    .setPositiveButton("OK")
    .show();

    // Third: custom dialog style.
    SettingService settingService = AndPermission.defineSettingDialog(activity, 400);
    ...
    // Called when the user clicks the [OK] button:
    settingService.execute();
    // Called when the user clicks the [NO] button:
    settingService.cancel();
}
```

If you are in the `Activity / Fragment` call the above code, when the user in the system `Setting` operation is completed, it will call back the` Activity/Fragment` this method:  
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
        case 400: { // This is the 400 you are above the number of incoming.
            // Check the permissions, and make the appropriate operation.
            if (AndPermission.hasPermissions(...)) {
                // TODO ...
            }
            break;
        }
    }
}
```

# Proguard-rules
1. If you use `Listener` to accept callback results, do not have any configuration.  
2. Use the annotation of the way callback results, in the `proguard` add the following configuration:  
```
-keepclassmembers class ** {
    @com.yanzhenjie.permission.PermissionYes <methods>;
}
-keepclassmembers class ** {
    @com.yanzhenjie.permission.PermissionNo <methods>;
}
```

# License
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