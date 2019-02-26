# Permission

In AndPermission, permissions and permission groups are placed in the class `Permission.java`.

## Reference permissions constant
We should use `Permission` to refer to permissions.

For example, referencing a single permission:
```java
Permission.CAMERA

Permission.CALL_PHONE
...
```

For example, referencing a single permission group:
```
Permission.Group.LOCATION

Permission.Group.PHONE
...
```

## Turn permissions into text
When we request some permissions failed, we should prompt the user to set permissions to allow these, For convenience, AndPermisison provides a way to turn permissions into prompt strings:
```java
/**
 * Turn permissions into text.
 */
public static List<String> transformText(Context context, String... permissions);

/**
 * Turn permissions into text.
 */
public static List<String> transformText(Context context, String[]... groups);

/**
 * Turn permissions into text.
 */
public static List<String> transformText(Context context, List<String> permissions)；
```

For example, let's add some permissions, assuming these permissions are the request failed:
```
List<String> deniedPermissions = new ArrayList<>();
deniedPermissions.add(Permission.READ_SMS);
deniedPermissions.add(Permission.CALL_PHONE);
deniedPermissions.add(Permission.READ_EXTERNAL_STORAGE);

List<String> permissionNames = Permission.transformText(context, deniedPermissions);
String permissionText = TextUtils.join(",\n", permissionNames);
```

At this point, `permissionText` result is:
```text
Sms
Phone
Storage
```
We can prompt the user to allow these permissions in the system Setting. This method supports internationalization.

**Special Note**: The `AndPermission.hasAlwaysDeniedPermission()` can only be called from the `onDenied()` callback and can not be used elsewhere.