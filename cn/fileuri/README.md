# 共享私有文件

AndPermission提供了兼容Android7.0及更高系统生成私有文件的Uri的方法：
```java
File file = ...;

Uri uri = AndPermission.getFileUri(context, file);
```

例如，在Android7.0或者更高系统中安装Apk:
```java
File apkFile = ...;

Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
Uri uri = AndPermission.getFileUri(context, apkFile);
intent.setDataAndType(uri, "application/vnd.android.package-archive");
context.startActivity(intent);
```

推荐链接：[在Android7.0和Android8.0中安装Apk](../install/README.md)。