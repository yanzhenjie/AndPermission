# Sharing private files

AndPermission provides a method for sharing files Uri compatible with Android 7.0 and higher:
```java
File file = ...;

Uri uri = AndPermission.getFileUri(context, file);
```

For example, when installing Apk on Android 7.0 or higher:
```java
File apkFile = ...;

Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
Uri uri = AndPermission.getFileUri(context, apkFile);
intent.setDataAndType(uri, "application/vnd.android.package-archive");
context.startActivity(intent);
```

Recommended link: [Installing Apk on Android 7.0 and Android 8.0](../install/README.md).