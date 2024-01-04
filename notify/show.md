# Request to send notifications

Starting with Android 8.0, users can manage the permissions of the app to show notifications. This permission is enabled by default.

We should check if the app has permission to show notifications at the right time, if not, the user should be directed to open the permission to show notifications for our app:
```java
AndPermission.with(this)
    .notification()
    .permission()
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
            // App can show notifications.
        }
     })
    .onDenied(new Action<Void>() {
        @Override
        public void onAction(Void data) {
            // App cann't show notifications.
        }
     })
     .start();
```

When `showRationale()` is called back without the **Show Notifications** permission, the developer must call `RequestExecutor#execute()` to initiate the setting or `RequestExecutor#cancel()` to cancel the startup setting, ortherwise AndPermission will have no response.

When `showRationale()` is called back it is a good idea to display a Dialog to solicit the user's comments, whether to launch the setup page authorization **Show Notifications** permissions, call `RequestExecutor#execute()` or `RequestExecutor#cancel()` depending on the user's choice.