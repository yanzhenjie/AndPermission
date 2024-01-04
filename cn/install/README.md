# 请求安装未知来源Apk

使用AndPermission也需要在`manifest.xml`中添加`android.permission.REQUEST_INSTALL_PACKAGES`权限，并且调用`AndPermission`的安装代码之前需要保证App拥有**外部存储设备读写**权限。

最简单的情况，只需要传入apk文件路径即可完成整个安装流程：
```java
File file = ...;

AndPermission.with(this)
    .install()
    .file(file)
    .start();
```

此时如果没有安装未知来源应用的权限，AndPermission将会自动启动安装权限设置页面让用户授权。

如果开发者想把体验做的更好一点，例如没有权限时弹一个`Dialog`让用户选择是否启动设置，用户同意或者拒绝后做一个统计：
```java
File apkFile = ...;

AndPermission.with(this)
    .install()
    .file(apkFile)
    .rationale(new Rationale<File>() {
        @Override
        public void showRationale(Context c, File f, RequestExecutor e) {
            // 没有权限会调用该访问，开发者可以在这里弹窗告知用户无权限。
            // 启动设置：e.execute();
            // 取消启动：e.cancel();
        }
    })
    .onGranted(new Action<File>() {
        @Override
        public void onAction(File data) {
        }
    })
    .onDenied(new Action<File>() {
        @Override
        public void onAction(File data) {
        }
    })
    .start();
```

当`showRationale()`被回调后说明没有`REQUEST_INSTALL_PACKAGES`权限，此时开发者必须回调`RequestExecutor#execute()`来启动设置或者`RequestExecutor#cancel()`来取消启动设置，否则将不会回调`onGranted()`或者`onDenied()`中的任何一个，也就是说AndPermission将不会有任何响应。

当`showRationale()`被回调时正确的做法是显示一个Dialog征求用户意见，是否要启动设置页面授权**安装未知来源应用**权限，根据用户选择调用`RequestExecutor#execute()`或者`RequestExecutor#cancel()`。

当`onGranted()`被回调时说明用户同意了权限，并会立刻进入安装界面，用户点击安装系统将会自动安装目标Apk到系统中；当`onDenied()`被回调时说明用户拒绝了权限，并会立刻退出安装界面。开发者可以在这两个方法被调用时做一些必要的统计。