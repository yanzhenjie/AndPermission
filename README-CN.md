我的主页：[http://www.yanzhenjie.com](http://www.yanzhenjie.com)  
欢迎关注我的微博：[http://weibo.com/yanzhenjieit](http://weibo.com/yanzhenjieit)  

**QQ技术交流群：[547839514](https://jq.qq.com/?_wv=1027&k=4Ev0ksp)**  

AndPermission是一个运行权限管理库，兼容Android O，同时最大程度上兼容了国产机。  
[Android8.0运行时权限策略变化和适配方案](http://blog.csdn.net/yanzhenjie1003/article/details/76719487)  

----
# 特性
1. 支持申请权限组，兼容Android8.0，最大程度上兼容国产机。
2. 链式调用，一句话申请权限，不需要判断版本和是否拥有某权限。
3. 支持注解回调结果、支持`Listener`回调结果。
4. 对于某个权限拒绝过一次后，下次申请可以使用`RationaleDailog`提示用户权限的重要性，面得被用户勾选**不再提示**从而再也申请不了权限（只能在系统`Setting`中授权）。
5. 就算用户拒绝权限并勾选**不再提示**，可使用`SettingDialog`提示用户去设置中授权。
6. `RationaleDialog`和`SettingDialog`允许开发者自定义。
7. `AndPermission`自带默认对话框除可自定义外，也支持国际化。
8. 支持在任何地方申请权限，不仅限于`Activity`和`Fragment`等。

# 引用方法
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

# 使用介绍
我建议下载`Demo`并阅读`README`会帮助你理解。

## 申请权限
**特别注意**：你在申请权限之前不需要判断版本和是否拥有某权限。
```java
// 在Activity：
AndPermission.with(activity)
    .requestCode(100)
    .permission(Permission.SMS)
    .rationale(...)
    .callback(...)
    .start();

// 在Fragment：
AndPermission.with(fragment)
    .requestCode(101)
    .permission(
        // 申请多个权限组方式：
        Permission.LOCATION,
        Permissioin.STORAGE
    )
    .rationale(...)
    .callback(...)
    .start();

// 在其它任何地方：
AndPermission.with(context)
    .requestCode(102)
    .permission(Permission.LOCATION)
    .rationale(...)
    .callback(...)
    .start();

// 如果你不想申请权限组，仅仅想申请某一个权限：
AndPermission.with(this)
    .requestCode(300)
    .permission(Manifest.permission.WRITE_CONTACTS)
    .rationale(...)
    .callback(...)
    .start();

// 如果你不想申请权限组，仅仅想申请某几个权限：
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

如果你会用了，你就可以大刀阔斧的干了，其它复杂的判断逻辑，`AndPermission`自动完成。

## Rationale能力
`Android`运行时权限有一个特点，在拒绝过一次权限后，再此申请该权限，在申请框会多一个**不再提示**的复选框，当用户勾选了**不再提示**并拒绝了权限后，下次再申请该权限将直接回调申请失败。  
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
        case 400: { // 这个400就是上面defineSettingDialog()的第二个参数。
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

我在开发中还遇到过一些情况，就不一一列举了，总结了一下这些情况都是回调结果和实际情况不符。

**建议一**：如果你担心使用标准的权限策略会使App崩溃，那么建议在回调的`成功`和`失败`方法中都加这段代码判断实际权限：
```
if(AndPermission.hasPermission()) {
    // TODO 执行拥有权限时的下一步。
} else {
    // 使用AndPermission提供的默认设置dialog，用户点击确定后会打开App的设置页面让用户授权。
    AndPermission.defaultSettingDialog(this, requestCode).show();
    
    // 建议：自定义这个Dialog，提示具体需要开启什么权限，自定义Dialog具体实现上面有示例代码。
}
```

`AndPermission.hasPermission()`的原理是在Android 6.0以下默认返回`true`，在6.0以上使用`AppOps`和`checkSelfPermission`检测权限全部通过则返回`true`，只有有一个没通过就返回`false`。

**建议二**：在实际开发中，比如小米手机，它有自己的一套权限管理系统，并不完全遵循系统的运行时权限策略，这种情况下的解决方案还是遵循**建议一**，但是不要使用`SettingDialog`的方式，而是直接提示用去打开系统`Setting`自行授权，或者你也可以在用户点击了**确定**按钮后直接打开系统`Setting`让用户授权。

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