# Request to install unknown source apk
Need to add `android.permission.REQUEST_INSTALL_PACKAGES` permission in `manifest.xml` before using, and to ensure that the app has **Storage** permissions.

In the simplest case, you only need to pass the path of the apk file to complete the installation process:
```java
File apkFile = ...;

AndPermission.with(this)
    .install()
    .file(apkFile)
    .start();
```

At this point, if you do not have permission to install an application from an unknown source, the setup page is automatically opened to request the user to open it.

If the developer wants to improve the experience. For example, if there is no permission, a dialog will be played to let the user choose whether to start the setting screen, and according to the user's choice to make a statistic:
```java
File apkFile = ...;

AndPermission.with(this)
    .install()
    .file(apkFile)
    .rationale(new Rationale<File>() {
        @Override
        public void showRationale(Context c, File f, RequestExecutor e) {
            // Startup settings: e.execute();
            // Cancel: e.cancel();
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

When `showRationale()` is called back without the `REQUEST_INSTALL_PACKAGES` permission, the developer must call `RequestExecutor#execute()` to initiate the setting or `RequestExecutor#cancel()` to cancel the startup setting, ortherwise AndPermission will have no response.

When `showRationale()` is called back it is a good idea to display a Dialog to solicit the user's comments, whether to launch the setup page authorization **Install Unknown Source App** permissions, call `RequestExecutor#execute()` or `RequestExecutor#cancel()` depending on the user's choice`.

When `onGranted()` is called back, the user agrees to the permission and will immediately enter the installation page, the user clicks on the installation system will automatically install the target Apk into the system; when `onDenied()` is called back, the user has denied the permission, and will immediately exit the installation page. Developers can do some necessary statistics when these two methods are called.