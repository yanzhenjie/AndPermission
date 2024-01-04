# 请求在其他App顶部绘制

使用AndPermission也需要在`manifest.xml`中添加`android.permission.SYSTEM_ALERT_WINDOW`权限。

从Android6.0开始使用`WindowManager.LayoutParams.TYPE_SYSTEM_ALERT`需要用户授权，从Android8.0开始它被替换为`WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY`，并且也需要用户授权。部分中国产手机在Android6.0以下就需要用户授权`WindowManager.LayoutParams.TYPE_SYSTEM_ALERT`，因此AndPermission也兼容了中国产手机Android6.0以下的系统。

最简单的情况，只需要在`onGrant()`的回调方法中执行关键代码即可：
```java
void showAlertWindow() {
    Dialog dialog = new Dialog(this);
    Window window = dialog.getWindow();

    int overlay = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
    int alertWindow = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
    int type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? overlay : alertWindow;
    window.setType(type);

    ...
    dialog.show();
}

AndPermission.with(this)
    .overlay()
    .onGranted(new Action<Void>() {
        @Override
        public void onAction(Void data) {
            showAlertWindow();
        }
    })
    .onDenied(new Action<Void>() {
        @Override
        public void onAction(Void data) {
            // TODO ...
        }
    })
    .start();
```

此时如果没有在其他App顶部绘制的权限，AndPermission将会自动启动该权限设置页面让用户授权。

如果开发者想把体验做的更好一点，例如没有权限时弹一个Dialog让用户选择是否启动设置：
```java
AndPermission.with(this)
    .overlay()
    .rationale(new Rationale<Void>() {
        @Override
        public void showRationale(Context c, Void d, RequestExecutor e) {
            // 启动设置: e.execute();
            // 取消启动: e.cancel();
        }
    })
    .onGranted(new Action<Void>() {
        @Override
        public void onAction(Void data) {
            showAlertWindow();
        }
    })
    .onDenied(new Action<Void>() {
        @Override
        public void onAction(Void data) {
            // TODO ...
        }
    })
    .start();
```

当`showRationale()`被回调后说明没有`SYSTEM_ALERT_WINDOW`权限，此时开发者必须回调`RequestExecutor#execute()`来启动设置或者`RequestExecutor#cancel()`来取消启动设置，否则将不会回调`onGranted()`或者`onDenied()`中的任何一个，也就是说AndPermission将不会有任何响应。