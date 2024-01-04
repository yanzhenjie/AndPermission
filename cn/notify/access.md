# 请求访问通知

从Android4.3开始，开发者可以访问通知（本质其实是监听通知被发送和移除），为了方便后面的讲解，这里做一个简单的演示，首先我们要新建一个`Service`，并继承`NotificationListenerService`：
```java
public class NotifyListenerService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }
}
```

因为`NotificationListenerService`是继承`Service`的，所以这是一个服务，因此我们需要在`manifest.xml`中注册它，另外还需要声明监听服务的权限，因此完整的服务注册代码是：
```xml
<service
    android:name=".NotifyListenerService"
    android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE">
        <intent-filter>
            <action android:name="android.service.notification.NotificationListenerService"/>
        </intent-filter>
</service>
```

抛开权限不谈，以上代码就可以实现访问通知了。然而，由于访问通知的权限默认是关闭的，因此我们需要用户手动开启这个权限。

## 请求访问通知权限
我们应该在合适的时机检测我们的App是否具有访问通知的权限，如果没有则弹窗告知用户，并引导用户前去设置中为我们的App开启访问通知的权限，使用AndPermission，开发者们可以这样做：
```java
AndPermission.with(this)
    .notification()
    .listener()
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
            // App可以访问通知了。
        }
    })
    .onDenied(new Action<Void>() {
        @Override
        public void onAction(Void data) {
            // App不能访问通知。
        }
    })
    .start();
```

当`showRationale()`被回调后说明没有访问通知的权限，此时开发者必须回调`RequestExecutor#execute()`来启动设置或者`RequestExecutor#cancel()`来取消启动设置，否则将不会回调`onGranted()`或者`onDenied()`中的任何一个，也就是说AndPermission将不会有任何响应。

当`showRationale()`被回调时正确的做法是显示一个Dialog征求用户意见，是否要启动设置页面授权**访问通知**权限，根据用户选择调用`RequestExecutor#execute()`或者`RequestExecutor#cancel()`。