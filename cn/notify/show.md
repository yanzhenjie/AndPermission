# 请求显示通知

从Android8.0开始，用户可以管理App显示通知的权限，该权限默认是开启的，但是部分国产机上是关闭的。

我们应该在合适的时机检测App是否具有显示通知的权限，如果没有，则弹窗告知用户并引导用户前去设置中为我们的App开启显示通知的权限，使用AndPermission，开发者们可以这样做：
```java
AndPermission.with(this)
    .notification()
    .permission()
    .rationale(new Rationale<Void>() {
        @Override
        public void showRationale(Context c, Void d, RequestExecutor e) {
            // 没有权限会调用该访问，开发者可以在这里弹窗告知用户无权限。
            // 启动设置: e.execute();
            // 取消启动: e.cancel();
        }
    })
    .onGranted(new Action<Void>() {
        @Override
        public void onAction(Void data) {
            // 可以发送通知。
        }
     })
    .onDenied(new Action<Void>() {
        @Override
        public void onAction(Void data) {
            // App不能发送通知。
        }
     })
     .start();
```

当`showRationale()`被回调后说明没有显示通知的权限，此时开发者必须回调`RequestExecutor#execute()`来启动设置或者`RequestExecutor#cancel()`来取消启动设置，否则将不会回调`onGranted()`或者`onDenied()`中的任何一个，也就是说AndPermission将不会有任何响应。

当`showRationale()`被回调时正确的做法是显示一个Dialog征求用户意见，是否要启动设置页面授权**显示通知**权限，根据用户选择调用`RequestExecutor#execute()`或者`RequestExecutor#cancel()`。