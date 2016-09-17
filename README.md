# AndPermission

严振杰的主页：[http://www.yanzhenjie.com](http://www.yanzhenjie.com)  
严振杰的博客：[http://blog.yanzhenjie.com](http://blog.yanzhenjie.com)  
技术交流群：547839514，加群前请务必阅读[群行为规范](https://github.com/yanzhenjie/SkillGroupRule)。  

更多解释请看[Android6.0 运行时权限管理最佳实践详解](http://blog.csdn.net/yanzhenjie1003/article/details/52503533)。

----

## 引用方法  
* AndroidStudio使用方法，gradle一句话远程依赖
```groovy
compile 'com.yanzhenjie:permission:1.0.1'
```
Or Maven:
```xml
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>permission</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

* Eclipse [下载jar包](https://github.com/yanzhenjie/AndPermission/blob/master/jar/andpermission.jar?raw=true)，或者自行下载源码。

我在开发SwipeRecyclerView时引用的RecyclerView版本如下：
```groovy
compile 'com.android.support:recyclerview-v7:23.4.0'
```

## 使用介绍
更好的例子，请下载源码后运行demo查看，这里给出最关键的代码。

### 申请权限就是这么简单
```java
AndPermission.with(this)
    .requestCode(101)
    .permission(Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.READ_SMS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
    .send();
```
只需要在Activity中或者Fragment中直接调用即可，AndPermission自动为你打理好后宫。

### 接受权限回调更简单
**方式一：利用Listener方式回调**
```java
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
// 只需要调用这一句，剩下的AndPermission自动完成。
AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
}

private PermissionListener listener = new PermissionListener() {
    @Override
    public void onSucceed(int requestCode) {
        if(requeust == 100) {
            ...
        } else if(requestCode == 101) {
            ...
        }
    }

    @Override
    public void onFailed(int requestCode) {
        if(requeust == 100) {
            ...
        } else if(requestCode == 101) {
            ...
        }
    }
};
```

**方式二：利用注解回调**
只需要重写Activity/Fragment的一个方法，然后提供一个授权时回调的方法即可：
```java
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    // 只需要调用这一句，剩下的AndPermission自动完成。
    AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
}

// 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
@PermissionYes(100)
private void getLocationYes() {
    // 申请权限成功，可以去做点什么了。
    Toast.makeText(this, "获取定位权限成功", Toast.LENGTH_SHORT).show();
}

// 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
@PermissionNo(100)
private void getLocationNo() {
    // 申请权限失败，可以提醒一下用户。
    Toast.makeText(this, "获取定位权限失败", Toast.LENGTH_SHORT).show();
}
```

只需要上面这么几句话即可，你就可以大刀阔斧的干了，在总结中提到的各种判断、复杂的情况AndPermission自动完成。

3、**如果你需要在用户多次拒绝权限后提示用户**
```java
AndPermission.with(this)
    .requestCode(101)
    .permission(Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.READ_SMS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
    .rationale(mRationaleListener)
    .send();

private RationaleListener mRationaleListener = new RationaleListener() {
    @Override
    public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
        new AlertDialog.Builder(RationalePermissionActivity.this)
            .setTitle("友好提醒")
            .setMessage("没有定位权限将不能为您推荐附近妹子，请把定位权限赐给我吧！")
            .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    rationale.resume();// 用户同意继续申请。
                }
            })
            .setNegativeButton("我拒绝", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    rationale.cancel(); // 用户拒绝申请。
                }
        }).show();
    }
};
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