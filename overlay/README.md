# Drawing at the top of other apps
Need to add `android.permission.SYSTEM_ALERT_WINDOW` permission in `manifest.xml` before using.

`WindowManager.LayoutParams.TYPE_SYSTEM_ALERT` requires user authorization at Android 6.0 or higher, it is replaced by `WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY` in Android 8.0 and requires user authorization.

In the simplest case, you just need to execute the dangerous code in the callback method of `onGrant()`:
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

At this point, if you don't have permission to draw on other apps, the setup page is automatically opened to request the user to open it.

If the developer wants to improve the experience. For example, if there is no permission, a dialog will be played to let the user choose whether to start the setting screen:
```java
AndPermission.with(this)
    .overlay()
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

When `showRationale()` is called back without the `SYSTEM_ALERT_WINDOW` permission, the developer must call `RequestExecutor#execute()` to initiate the setting or `RequestExecutor#cancel()` to cancel the startup setting, ortherwise AndPermission will have no response.