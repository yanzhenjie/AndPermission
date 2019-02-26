# Usage

AndPermission can request permission in Activity/Fragment, or wtih Context anywhere.
```
AndPermission.with(activity)
	...

// or
AndPermission.with(fragment)
	...

// or
AndPermission.with(context)
	...
```

## Basic Api
Request a single permission:
```java
AndPermission.with(this)
	.permission(Permission.WRITE_EXTERNAL_STORAGE)
	.onGranted(new Action() {
		@Override
		public void onAction(List<String> permissions) {
			// TODO what to do.
		}
	}).onDenied(new Action() {
		@Override
		public void onAction(List<String> permissions) {
			// TODO what to do
		}
	})
	.start();
```

Request a single permission group:
```java
AndPermission.with(this)
	.permission(Permission.Group.STORAGE)
	...
```

Request a few permissions:
```java
AndPermission.with(this)
	.permission(
		Permission.WRITE_EXTERNAL_STORAGE,
		Permission.CAMERA
	)
	...
```

Request a few permission groups:
```java
AndPermission.with(this)
	.permission(
		Permission.Group.STORAGE,
		Permission.Group.CAMERA
	)
	...
```

## When rejected
Users often do not authorize some permissions, and we happen to have to use these permissions, this time we should be friendly reminder user.  

For example, when the user denies `Permission.WRITE_EXTERNAL_STORAGE` once, the next time we request `Permission.WRITE_EXTERNAL_STORAGE`, we should show why this permission is needed so that users can determine if they need to be authorized. In AndPermission we can use Rationale.

```java
private Rationale mRationale = new Rationale() {
	@Override
	public void showRationale(Context context, List<String> permissions, 
			RequestExecutor executor) {
		// Here should display a Dialog to ask the user whether to continue.

		// When the user to continue the request:
		executor.execute();

		// When the user interrupts the request:
		executor.cancel();
	}
};

AndPermission.with(this)
	.permission(Permission.WRITE_EXTERNAL_STORAGE)
	.rationale(mRationale)
	.onGranted(...)
	.onDenied(...)
	.start();
```

## When always rejected
The application may not respond (not ANR) when a user clicks on one of the application's buttons and he always rejects one of the permissions we need. In order to avoid this situation, we should prompt the user to open permission in the system when the user always rejects the permission, regardless of whether the user will really allow.

```java
AndPermission.with(this)
	.permission(...)
	.rationale(...)
	.onGranted(...)
	.onDenied(new Action() {
		@Override
		public void onAction(List<String> permissions) {
			if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
				// These permissions are always rejected by the user.
			}
		}
	})
	.start();
```

Next you can prompt the user to open the system settings to allow us the required permissions.
```java
...

if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
	// Here should display a Dialog to ask the user whether to setting.

	SettingService settingService = AndPermission.permissionSetting(mContext);
	
	// When the user agrees:
	settingService.execute();

	// When using rejected
	settingService.cancel();
}
```

Of course, we need to prompt the user to allow what permissions, look here: [Permission](/permission.md)