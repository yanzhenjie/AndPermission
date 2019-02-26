# 权限

在AndPermission中，权限和权限组都在`Permission.java`中，方便开发者调用。

## 引用权限常量
我们都应该通过`Permission`引用权限和权限组。

例如，引用单个权限：
```java
Permission.CAMERA

Permission.CALL_PHONE
...
```

例如，引用单个权限组：
```
Permission.Group.LOCATION

Permission.Group.PHONE
...
```

## 转化权限字符串成文字
当我们请求某些权限失败时，我们应该提示去设置中授权某些权限，为了方便开发者，AndPermission提供了一个方法把权限字符串转为对应的提示文字。
```java
/**
 * 转化权限成文字。
 */
public static List<String> transformText(Context context, String... permissions);

/**
 * 转化权限成文字。
 */
public static List<String> transformText(Context context, String[]... groups);

/**
 * 转化权限成文字。
 */
public static List<String> transformText(Context context, List<String> permissions)；
```

例如，我们加入一些权限到List中，并假设这些权限是失败的：
```
List<String> deniedPermissions = new ArrayList<>();
deniedPermissions.add(Permission.READ_SMS);
deniedPermissions.add(Permission.CALL_PHONE);
deniedPermissions.add(Permission.READ_EXTERNAL_STORAGE);

List<String> permissionNames = Permission.transformText(context, deniedPermissions);
String permissionText = TextUtils.join(",\n", permissionNames);
```

此时, `permissionText`的结果是：
```text
短信
手机
存储空间
```
我可以提示用户去设置中允许这些权限了，这个方法支持国际化。