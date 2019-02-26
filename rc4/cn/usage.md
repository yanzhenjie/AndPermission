# 用法

AndPermission可以在Activity/Fragment中请求权限, 或者在任何地方使用Context请求权限：
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

## 基本的Api
请求单个权限：
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

请求单个权限组：
```java
AndPermission.with(this)
	.permission(Permission.Group.STORAGE)
	...
```

请求多个权限：
```java
AndPermission.with(this)
	.permission(
		Permission.WRITE_EXTERNAL_STORAGE,
		Permission.CAMERA
	)
	...
```

请求多个权限组：
```java
AndPermission.with(this)
	.permission(
		Permission.Group.STORAGE,
		Permission.Group.CAMERA
	)
	...
```

## 当被拒绝时
用户往往会拒绝一些权限，而程序的继续运行又必须使用这些权限，此时我们应该友善的提示用户。

例如，当用户拒绝`Permission.WRITE_EXTERNAL_STORAGE`一次，在下次请求`Permission.WRITE_EXTERNAL_STORAGE`时，我们应该展示为什么需要此权限的说明，以便用户判断是否需要授权给我们。在AndPermission中我们可以使用Rationale。

```java
private Rationale mRationale = new Rationale() {
	@Override
	public void showRationale(Context context, List<String> permissions, 
			RequestExecutor executor) {
		// 这里使用一个Dialog询问用户是否继续授权。

		// 如果用户继续：
		executor.execute();

		// 如果用户中断：
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

## 当总是被拒绝
当用户点击应用程序的某个按钮，而他又总是拒绝我们需要的某个权限时，应用程序可能不会响应（但不是ANR），为了避免这种情况，我们应该在用户总是拒绝某个权限时提示用户去系统设置中授权哪些权限给我们，无论用户是否真的会授权给我们。

```java
AndPermission.with(this)
	.permission(...)
	.rationale(...)
	.onGranted(...)
	.onDenied(new Action() {
		@Override
		public void onAction(List<String> permissions) {
			if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
				// 这些权限被用户总是拒绝。
			}
		}
	})
	.start();
```

接下来我们应该提示用户去系统设置中授权这些权限给我们。
```java
...

if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
	// 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。

	SettingService settingService = AndPermission.permissionSetting(mContext);
	
	// 如果用户同意去设置：
	settingService.execute();

	// 如果用户不同意去设置：
	settingService.cancel();
}
```

当然，我们需要提示用户应该授权哪些权限，看这里：[权限](/permission.md)

**特别注意**：`AndPermission.hasAlwaysDeniedPermission()`只能在`onDenied()`的回调中调用，不能在其它地方使用。