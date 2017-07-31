# AndPermission

严振杰的主页：[http://www.yanzhenjie.com](http://www.yanzhenjie.com)  
严振杰的博客：[http://blog.yanzhenjie.com](http://blog.yanzhenjie.com)  

**欢迎加入QQ技术交流群：[46523908](https://jq.qq.com/?_wv=1027&k=489vOpV)**  

关于Android运行时权限请看：[Android运行时权限管理最佳实践详解](http://blog.csdn.net/yanzhenjie1003/article/details/52503533)。  

对于国产手机运行结果和你的预期结果不一样，这可能是国产机的bug或者是特点，对此我给出了解决方案，请看[国产手机适配方案](#国产手机适配方案)，如果你有更好的方案，请提交PR或者发[issue](https://github.com/yanzhenjie/AndPermission/issues)告之我。  

----
# 特性
1. 链式调用，一句话申请权限，为你省去复杂的逻辑判断。
2. 支持注解回调结果、支持Listener回调结果。
3. **拒绝一次**某权限后，再次申请该权限时可使用`Rationale`向用户说明申请该权限的目的，在用户同意后再继续申请，避免用户勾选**不再提示**而导致不能再次申请该权限。
4. 就算用户拒绝权限并勾选**不再提示**，可使用`SettingDialog`提示用户去设置中授权。
5. `RationaleDialog`和`SettingDialog`允许开发者自定义。
6. `AndPermission`自带默认对话框除可自定义外，也支持国际化。
7. 支持在任何地方申请权限，不仅限于`Activity`和`Fragment`等。

# 引用方法
* Gradle
```groovy
compile 'com.yanzhenjie:permission:1.0.8'
```

* Maven
```xml
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>permission</artifactId>
  <version>1.0.8</version>
  <type>pom</type>
</dependency>
```

* Eclipse 请放弃治疗

# 使用介绍
我建议下载`Demo`并阅读`README`会帮助你理解。

## 申请权限
**特别注意：**你在申请权限之前不用进行任何判断，`AndPermission`内部已经做了判断，如果有权限不会重复申请的。
```java
// 在Activity：
AndPermission.with(activity)
    .requestCode(100)
    .permission(Manifest.permission.WRITE_CONTACTS)
    .rationale(...)
    .callback(...)
    .start();

// 在Fragment：
AndPermission.with(fragment)
    .requestCode(100)
    .permission(
        // 多个权限，以数组的形式传入。
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.READ_SMS
    )
    .rationale(...)
    .callback(...)
    .start();

// 在其它任何地方：
AndPermission.with(context)
    .requestCode(100)
    .permission(
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.READ_SMS
    )
    .rationale(...)
    .callback(...)
    .start();
```


## 接受回调结果
接受回调结果目前有两种方式：一、`Listener`方式，二、注解方式。

### 方式一：Listener方式回调
在`callback()`方法传入`PermissionListener`即可，授权成功或者失败至少会回调其中一个方法。
```java
AndPermission.with(context)
    ...
    .requestCode(200)
    .callback(listener)
    .start();

private PermissionListener listener = new PermissionListener() {
    @Override
    public void onSucceed(int requestCode, List<String> grantedPermissions) {
        // 权限申请成功回调。
        
        // 这里的requestCode就是申请时设置的requestCode。
        // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
        if(requestCode == 200) {
            // TODO ...
        }
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        // 权限申请失败回调。
        if(requestCode == 200) {
            // TODO ...
        }
    }
};
```

### 方式二：注解方式回调
在`callback()`方法传入你的回调方法所在实例的对象即可。
```java
AndPermission.with(context)
    ...
    .requestCode(300)
    .callback(this)
    .start();

// 成功回调的方法，用注解即可，这里的300就是请求时的requestCode。
@PermissionYes(300)
private void getPermissionYes(List<String> grantedPermissions) {
    // TODO 申请权限成功。
}

@PermissionNo(300)
private void getPermissionNo(List<String> deniedPermissions) {
    // TODO 申请权限失败。
}
```

如果你会用了，你就可以大刀阔斧的干了，博客中讲到的各种复杂逻辑，`AndPermission`自动完成。

## Rationale能力
`Android`运行时权限有一个特点，在拒绝过一次权限后，再此申请该权限，在申请框会多一个**[不再提示]**的复选框，当用户勾选了**[不再提示]**并拒绝了权限后，下次再申请该权限将直接回调申请失败。  
因此`Rationale`功能是在用户拒绝一次权限后，再次申请时检测到已经申请过一次该权限了，允许开发者弹窗说明申请权限的目的，获取用户的同意后再申请权限，避免用户勾选不再提示，导致不能再次申请权限。  

### 方式一：使用AndPermssion默认MD风格对话框
```java
AndPermission.with(this)
    ...
    .requestCode(...)
    .rationale((requestCode, rationale) ->
        // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
        AndPermission.rationaleDialog(context, rationale).show()
    )
    .start()
```

### 方式二：自定义对话框
```java
AndPermission.with(this)
    ...
    .requestCode(...)
    .rationale(rationaleListener)
    .start()

/**
 * Rationale支持，这里自定义对话框。
 */
private RationaleListener rationaleListener = (requestCode, rationale) -> {
    AlertDialog.newBuilder(this)
        .setTitle("友好提醒")
        .setMessage("你已拒绝过定位权限，沒有定位定位权限无法为你推荐附近的妹子，你看着办！")
        .setPositiveButton("好，给你", (dialog, which) -> {
            rationale.resume();
        })
        .setNegativeButton("我拒绝", (dialog, which) -> {
            rationale.cancel();
        }).show();
};
```

## 提示用户在系统设置中授权
当用户拒绝权限并勾选了不再提示时，此时再次申请权限时将会直接回调申请失败，因此`AndPermission`提供了一个供用户在系统`Setting`中给我们授权的能力。  

我们在授权失败的回调方法中添加如下代码，以下三种选择一种即可：
```java
// 是否有不再提示并拒绝的权限。
if (AndPermission.hasAlwaysDeniedPermission(activity, deniedPermissions)) {
    // 第一种：用AndPermission默认的提示语。
    AndPermission.defaultSettingDialog(activity, 400).show();

    // 第二种：用自定义的提示语。
    AndPermission.defaultSettingDialog(activity, 400)
    .setTitle("权限申请失败")
    .setMessage("您拒绝了我们必要的一些权限，已经没法愉快的玩耍了，请在设置中授权！")
    .setPositiveButton("好，去设置")
    .show();

    // 第三种：自定义dialog样式。
    SettingService settingService = AndPermission.defineSettingDialog(activity, 400);
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
        case 400: { // 这个400就是你上面传入的数字。
            // 你可以在这里检查你需要的权限是否被允许，并做相应的操作。
            break;
        }
    }
}
```

# 混淆
1. 如果使用`Listener`接受回调结果，不用任何配置。  
2. 使用注解的方式回调结果，在`proguard`中添加如下配置：
```
-keepclassmembers class ** {
    @com.yanzhenjie.permission.PermissionYes <methods>;
}
-keepclassmembers class ** {
    @com.yanzhenjie.permission.PermissionNo <methods>;
}
```

# 国产手机适配方案
**AndPermission**是严格按照`Android`系统的`运行时权限`设计的，并最大限度上兼容了国产手机，目前发现的国产手机bug及解决方案：  

1. 部分中国厂商生产手机（例如小米某型号）的`Rationale`功能，在第一次拒绝后，第二次申请时不会返回`true`，并且会回调申请失败，也就是说在第一次决绝后默认勾选了**不再提示**，所以建议一定使用`SettingDialog`：[提示用户在系统设置中授权](#提示用户在系统设置中授权)。

2. 部分中国厂商生产手机（例如小米、华为某型号）在申请权限时，用户点击确定授权后，还是回调我们申请失败，这个时候其实我们是拥有权限的，建议在**失败**的回调房中调用`AppOpsManager`做权限判断：
 `if(AndPermission.hasPermission()) {// 执行操作。}`

3. 部分中国厂商生产手机在系统`Setting`中设置**[禁用/询问]**某权限，但是在申请此权限时却直接提示有权限，**这可能是厂商故意这样设计的，**当我们真正执行需要这个权限代码的时候系统会自动申请权限，但是为了兼容到其它手机，建议在**成功**的回调房中调用`AppOpsManager`做权限判断：
 `if(AndPermission.hasPermission()) {// 执行操作。}`

4. 部分中国厂商生产手机（例如vivo、pppo某型号）在用户允许权限，并且回调了权限授权成功的方法，但是实际执行代码时并没有这个权限，建议在**成功**的回调房中调用`AppOpsManager`做权限判断：
 `if(AndPermission.hasPermission()) {// 有权限。}`

5. 部分开发者反馈，在某些手机的`Setting`中授权后，检查时还是没有权限，执行响应的代码时应用崩溃（错误提示是没有权限），这种手机真的兼容不到了，我也觉得没必要兼容了，建议直接放弃这种平台。

**建议：**建议在上述`if(AndPermission.hasPermission()) {}`加个`else{}`操作，并在`else{}`中使用`AndPermission`提供的`SettingDialog`能力提示用户去系统`Setting`中开启权限。

> 最后希望咱中国Android手机厂商早日修复这些问题，祝你们事业越来越成功，产品越做越好。

# License
```text
Copyright © Yan Zhenjie

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