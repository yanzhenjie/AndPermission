# Request to access notifications

Starting with Android 4.3, developers can access notifications, Let's write an example:
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

The essence of `NotificationListenerService` is `Service`, so we need to register it in the `manifest.xml` and declare the permission:
```xml
<service
    android:name=".NotifyListenerService"
    android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE">
        <intent-filter>
            <action android:name="android.service.notification.NotificationListenerService"/>
        </intent-filter>
</service>
```

Temporarily abandoning the permissions does not care, the above code can access the notification. However, since the access notification permission is turned off by default, we need the user to manually open the permission.

## Request access notification permission
We should check if the app has permission to access notifications at the right time, if not, the user should be directed to open the permission to access notifications for our app:
```java
AndPermission.with(this)
    .notification()
    .listener()
    .rationale(new Rationale<Void>() {
        @Override
        public void showRationale(Context c, Void d, RequestExecutor e) {
            // Startup settings: e.execute();
            // Cancel: e.cancel();
        }
    })
    .onGranted(new Action<Void>() {
        @Override
        public void onAction(Void data) {
            // App can access notifications.
        }
    })
    .onDenied(new Action<Void>() {
        @Override
        public void onAction(Void data) {
            // App cann't access notifications.
        }
    })
    .start();
```

When `showRationale()` is called back without the **Access Notifications** permission, the developer must call `RequestExecutor#execute()` to initiate the setting or `RequestExecutor#cancel()` to cancel the startup setting, ortherwise AndPermission will have no response.

When `showRationale()` is called back it is a good idea to display a Dialog to solicit the user's comments, whether to launch the setup page authorization **Access Notifications** permissions, call `RequestExecutor#execute()` or `RequestExecutor#cancel()` depending on the user's choice.