# AndPermission

严振杰的主页：[http://www.yanzhenjie.com](http://www.yanzhenjie.com)
严振杰的博客：[http://blog.yanzhenjie.com](http://blog.yanzhenjie.com)

**欢迎加入QQ技术交流群：[46523908](http://jq.qq.com/?_wv=1027&k=40hvC7E)**

关于Android运行时权限6.0请看：[Android6.0 运行时权限管理最佳实践详解](http://blog.csdn.net/yanzhenjie1003/article/details/52503533)。

> 以下说明只针对部分**国产手机**：部分人反馈，申请时没有弹窗直接回调了拥有权限，而在系统的Setting中看到是没有这个权限的。这种情况，请直接调用相应功能，如果能正常调用，说明App是拥有权限的，所以是系统Setting的问题。如果申请时允许了权限，但是回调了失败，说明是系统发放权限失败，系统返回了失败，也是系统bug，AndPermission提供了一个在onFailed时提示用户去Setting设置的dialog，点击确定即可跳转Setting，后文有介绍，demo中也有例子。

----
# 特性
1. **兼容国产手机**（已测试通过：小米、华为、Oppo、魅族、Moto，以及Nexus、三星...）。
2. 支持链式调用，一句话申请权限，为你省去复杂的逻辑判断。
3. 支持注解回调结果、支持Listener回调结果。
4. （可选）支持Rationale，**拒绝一次**权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选**不再提示**。
5. （可选）就算用户勾选**不再提示**，并且**拒绝权限**后，AndPermission智能提示用户去设置中授权。
6. Rationale对话框自定义，申请授权失败后打开设置的dialog，预置了MD风格dialog，开发者也可以自定义。
7. 如果使用默认对话框，自动支持国际化。
8. 支持`Activity`、`android.support.v4.app.Fragment`、`android.app.Fragment`。

# 引用方法
* AndroidStudio使用方法，gradle一句话远程依赖
```groovy
compile 'com.yanzhenjie:permission:1.0.5'
```
Or Maven:
```xml
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>permission</artifactId>
  <version>1.0.5</version>
  <type>pom</type>
</dependency>
```

* Eclipse [下载jar包](https://github.com/yanzhenjie/AndPermission/blob/master/jar/andpermission.jar?raw=true)，并依赖Support的AppCompat项目。

# 使用介绍
我建议还是直接下载上方的demo和源码运行一把，一切尽在掌握。

## 申请权限
**一个权限**
```java
AndPermission.with(this)
    .requestCode(100)
    .permission(Manifest.permission.WRITE_CONTACTS)
    .send();
```

**多个权限**
```java
AndPermission.with(this)
    .requestCode(100)
    .permission(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS)
    .send();
```
在使用到特殊权限时，只需要在`Activity`、`Fragment`中直接调用，等到`AndPermission`回调时即可执行相应的代码。

**注意**
1. 如果你的`Activity`继承的是`AppCompatActivity`、`FragmentActivity`或者它们的子类，那么你直接请求权限就可以。
2. 如果你的`Fragment`继承的是`android.support.v4.app.Fragment`或者它的子类，那么你直接请求权限就可以。
3. 如果你继承的是`android.app.Activity`、`android.app.Fragment`、在6.0以下的手机是没有`onRequestPermissionsResult()`方法的，所以需要在申请权限前判断：
```java
// 先判断是否有权限。
if(AndPermission.hasPermission(this, Manifest.permission.READ_SMS)) {
    // 有权限，直接do anything.
} else {
    // 申请权限。
    AndPermission.with(this)
        .requestCode(100)
        .permission(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS)
        .send();
}
```

## 回调结果
**方式一：利用Listener方式回调**
```java
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
    AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
}

private PermissionListener listener = new PermissionListener() {
    @Override
    public void onSucceed(int requestCode, List<String> grantedPermissions) {
        // 权限申请成功回调。
        if(requeust == 100) {
            // TODO 相应代码。
        } else if(requestCode == 101) {
            // TODO 相应代码。
        }
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        // 权限申请失败回调。

        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();

            // 第二种：用自定义的提示语。
            // AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
            // .setTitle("权限申请失败")
            // .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
            // .setPositiveButton("好，去设置")
            // .show();

            // 第三种：自定义dialog样式。
            // SettingService settingService =
            //    AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
            // 你的dialog点击了确定调用：
            // settingService.execute();
            // 你的dialog点击了取消调用：
            // settingService.cancel();
        }
    }
};
```

**方式二：利用注解回调**
只需要重写Activity/Fragment的一个方法，然后提供一个授权时回调的方法即可：
```java
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    // 只需要调用这一句，第一个参数是当前Acitivity/Fragment，回调方法写在当前Activity/Framgent。
    AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
}

// 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
@PermissionYes(100)
private void getLocationYes(List<String> grantedPermissions) {
    // TODO 申请权限成功。
}

// 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
@PermissionNo(100)
private void getLocationNo(List<String> deniedPermissions) {
    // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
    if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
        // 第一种：用默认的提示语。
       AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();
    }
}
```

如果你会用了，你就可以大刀阔斧的干了，博客中讲到的各种复杂逻辑，AndPermission自动完成。

## Rationale拒绝一次后，再次提示用户权限作用
**方式一：使用AndPermssion默认MD风格对话框**
```java
AndPermission.with(this)
    .requestCode(REQUEST_CODE_PERMISSION_LOCATION)
    .permission(Manifest.permission.ACCESS_FINE_LOCATION)
    .rationale((requestCode, rationale) ->
        // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
        AndPermission.rationaleDialog(PermissionActivity.this, rationale).show()
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
    AlertDialog.build(this)
        .setTitle("友好提醒")
        .setMessage("您已拒绝过定位权限，没有定位权限无法为您推荐附近妹子，请把定位权限赐给我吧！")
        .setPositiveButton("好，给你", (dialog, which) -> {
            rationale.resume();
        })
        .setNegativeButton("我拒绝", (dialog, which) -> {
            rationale.cancel();
        }).show();
};
```

# 混淆
**1. 如果使用Listener接受回调结果，不用任何配置。**

**2. 使用注解的方式回调结果，在proguard-rules.pro中添加如下配置**
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
Copyright 2017 Yan Zhenjie

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
