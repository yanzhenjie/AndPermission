# AndPermission

严振杰的主页：[http://www.yanzhenjie.com](http://www.yanzhenjie.com)  
严振杰的博客：[http://blog.yanzhenjie.com](http://blog.yanzhenjie.com)  

**欢迎加入QQ技术交流群：[46523908](http://jq.qq.com/?_wv=1027&k=40hvC7E)**  

关于Android运行时权限6.0请看：[Android6.0 运行时权限管理最佳实践详解](http://blog.csdn.net/yanzhenjie1003/article/details/52503533)。  

对于国产手机，我给出了兼容方案，请看[关于国产手机的兼容方案](#关于国产手机的兼容方案)，如果你有更好的方案，也请发[issue](https://github.com/yanzhenjie/AndPermission/issues)告之我，更加欢迎guys直接提PR上来。  

----
# 特性
1. 链式调用，一句话申请权限，为你省去复杂的逻辑判断。
2. 支持注解回调结果、支持Listener回调结果。
3. **拒绝一次**权限后，再次申请时先征求用户同意再申请授权，避免用户勾选**不再提示**，导致不能再次申请权限。
4. 就算用户拒绝权限并勾选**不再提示**，可使用AndPermission的SettingDialog提示用户去设置中授权。
5. RationaleDialog和SettingDialog对话框允许自定义。
6. 默认对话框支持国际化。
7. 支持在任何地方申请权限，不仅限于`Activity`、`Fragment`。

# 引用方法
* AndroidStudio使用方法，gradle一句话远程依赖
```groovy
compile 'com.yanzhenjie:permission:1.0.6'
```
Or Maven:
```xml
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>permission</artifactId>
  <version>1.0.6</version>
  <type>pom</type>
</dependency>
```

* Eclipse 请放弃治疗

# 使用介绍
我建议还是直接下载上方的demo和源码运行一把，一切尽在掌握。

## 申请权限
```java
// 在Activity：
AndPermission.with(activity)
    .requestCode(100)
    .permission(Manifest.permission.WRITE_CONTACTS)
    .callback(...)
    .send();

// 在Fragment：
AndPermission.with(fragment)
    .requestCode(100)
    .permission(
        // 多个权限，以数组的形式传入。
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.READ_SMS
    )
    .callback(...)
    .send();

// 在其它任何地方：
AndPermission.with(context)
    .requestCode(100)
    .permission(
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.READ_SMS
    )
    .callback(...)
    .send();
```


## 接受回调结果
**方式一：利用Listener方式回调：**在`callback()`方法传入`PermissionListener`即可。
```java
AndPermission.with(context)
    ...
    .requestCode(100)
    .callback(listener)
    .send();

private PermissionListener listener = new PermissionListener() {
    @Override
    public void onSucceed(int requestCode, List<String> grantedPermissions) {
        // 权限申请成功回调。
        
        // 这里的requestCode就是申请的设置的requestCode。
        // 可以用来区分多个不同的申请，做相应的操作。
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        // 权限申请失败回调。
    }
};
```

**方式二：利用注解回调：**在`callback()`方法传入你在回调方法所以在实例对象即可。
```java
AndPermission.with(context)
    ...
    .callback(this)
    .send();

// 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
@PermissionYes(100)
private void getPermissionYes(List<String> grantedPermissions) {
    // TODO 申请权限成功。
}

@PermissionNo(100)
private void getPermissionNo(List<String> deniedPermissions) {
    // TODO 申请权限失败。
}
```

如果你会用了，你就可以大刀阔斧的干了，博客中讲到的各种复杂逻辑，AndPermission自动完成。

## Rationale能力
运行时权限有一个特点，在拒绝过一次权限后，再此申请该权限，会在申请框多一个**[不再提示]**的复选框，当用户勾选了不再提示并拒绝了权限后，下次再申请该权限将直接回调申请失败。  
所以Rationale功能是在用户拒绝一次权限后，再次申请时提示用户我们要申请权限了，获取用户的同意后再申请权限，避免用户勾选不再提示，导致不能再次申请权限。  

**方式一：使用AndPermssion默认MD风格对话框**
```java
AndPermission.with(this)
    .requestCode(REQUEST_CODE_PERMISSION_LOCATION)
    .permission(Manifest.permission.ACCESS_FINE_LOCATION)
    .rationale((requestCode, rationale) ->
        // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
        AndPermission.rationaleDialog(context, rationale).show()
    )
    .send()
```

**方式二：自定义对话框**
```java
AndPermission.with(this)
    .requestCode(REQUEST_CODE_PERMISSION_LOCATION)
    .permission(Manifest.permission.ACCESS_FINE_LOCATION)
    .rationale(rationaleListener)
    .send()

/**
 * Rationale支持，这里自定义对话框。
 */
private RationaleListener rationaleListener = (requestCode, rationale) -> {
    AlertDialog.newBuilder(this)
        .setTitle("友好提醒")
        .setMessage("您已拒绝过定位权限，没有定位权限无法为您推荐附近妹子，你看着办！")
        .setPositiveButton("好，给你", (dialog, which) -> {
            rationale.resume();
        })
        .setNegativeButton("我拒绝", (dialog, which) -> {
            rationale.cancel();
        }).show();
};
```

## 提示用户在系统设置中授权
当用户拒绝权限并勾选了不再提示时，此时再次申请权限时将会直接回调申请失败，因此AndPermission提供了一个供用户在系统`Setting`中给我们授权的能力。  

我们在授权失败的回调方法中添加如下代码，以下三种选择一种即可：
```java
// 是否有不再提示并拒绝的权限。
if (AndPermission.hasAlwaysDeniedPermission(activity, deniedPermissions)) {
    // 第一种：用默认的提示语。
    AndPermission.defaultSettingDialog(activity, 100).show();

    // 第二种：用自定义的提示语。
    AndPermission.defaultSettingDialog(activity, 100)
    .setTitle("权限申请失败")
    .setMessage("您拒绝了我们必要的一些权限，已经没法愉快的玩耍了，请在设置中授权！")
    .setPositiveButton("好，去设置")
    .show();

    // 第三种：自定义dialog样式。
    SettingService settingService = AndPermission.defineSettingDialog(activity, 100);
    ...
    // 你的dialog点击了确定调用：
    settingService.execute();
    // 你的dialog点击了取消调用：
    settingService.cancel();
}
```

如果你是在`Activity/Fragment`中调用的上述代码，那么当用户在系统`Setting`中操作完成后，会回调`Activity/Fragment`中的这个方法：  
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
        case 100: { // 这个100就是你上面传入的数字。
            // 你可以在这里检查你需要的权限是否被允许，并做相应的操作。
            break;
        }
    }
}
```

# 混淆
1. 如果使用Listener接受回调结果，不用任何配置。  
2. 使用注解的方式回调结果，在proguard-rules.pro中添加如下配置。
```
-keepclassmembers class ** {
    @com.yanzhenjie.permission.PermissionYes <methods>;
}
-keepclassmembers class ** {
    @com.yanzhenjie.permission.PermissionNo <methods>;
}
```

# 关于国产手机的兼容方案
**AndPermission**是严格按照`Android`系统的`运行时权限`设计的，并最大限度上兼容了国产手机，目前发现的国产手机bug及解决方案：  

* 部分中国厂商生产手机（例如小米某型号）的`Rationale`功能，在第一次拒绝后，第二次申请时不会返回`true`，并且会回调申请失败，也就是说在第一次决绝后默认勾选了`不再提示`，所以建议一定使用`SettingDialog`：[提示用户在系统设置中授权](#提示用户在系统设置中授权)。
* 部分中国厂商生产手机（例如小米、华为某型号）在申请权限时，用户点击确定授权后，还是回调我们申请失败，这个时候其实我们是拥有权限的，所以我们可以在失败的方法中使用`AppOpsManager`进行权限判断，`AndPermission`已经封装好了：
```
if(AndPermission(context, permission1, permission2)) {
    // 执行操作。
}
```
* 部分中国厂商生产手机（例如vivo、pppo某型号）在用户允许权限，并且回调了权限授权陈功的方法，但是实际执行代码时并没有这个权限，建议开发者在回调成功的方法中也利用`AppOpsManager`判断下：
```
if(AndPermission(context, permission1, permission2)) {
    // 执行操作。
} else {
    // 提醒用户手机问题，请用户去Setting中授权。这里可以使用AndPermission的SettingDialog。
}
```
* 部分开发者反馈，在某些手机的`Setting`中授权后实际，检查时还是没有权限，部分执行代码也是没有权限，这种手机真的兼容不到了，我也觉得没必要兼容了，建议直接放弃这种平台。

> 最后希望中国Android手机厂商早日修复这些问题，祝你们事业越来越成功，产品越做越好。

# License
```text
Copyright © Yan Zhenjie. All Rights Reserved

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
