/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.permission;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yanzhenjie.permission.source.AppActivitySource;
import com.yanzhenjie.permission.source.ContextTarget;
import com.yanzhenjie.permission.source.FragmentSource;
import com.yanzhenjie.permission.source.Source;
import com.yanzhenjie.permission.source.SupportFragmentSource;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Request the entrance.</p>
 * Created by Yan Zhenjie on 2016/9/9.
 */
public class AndPermission {

    /**
     * Check if the calling context has a set of permissions.
     *
     * @param context     {@link Context}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        return hasPermission(context, Arrays.asList(permissions));
    }

    /**
     * Check if the calling context has a set of permissions.
     *
     * @param context     {@link Context}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        for (String permission : permissions) {
            int result = context.checkPermission(permission, android.os.Process.myPid(), Process.myUid());
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }

            String op = AppOpsManager.permissionToOp(permission);
            if (TextUtils.isEmpty(op)) {
                return false;
            }

            AppOpsManager appOpsManager = context.getSystemService(AppOpsManager.class);
            result = appOpsManager.noteProxyOp(op, context.getPackageName());
            if (result != AppOpsManager.MODE_ALLOWED) {
                return false;
            }

        }
        return true;
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param activity          {@link Activity}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull Activity activity,
            @NonNull List<String> deniedPermissions) {
        return hasAlwaysDeniedPermission(new AppActivitySource(activity), deniedPermissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment    {@link android.support.v4.app.Fragment}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull android.support.v4.app.Fragment fragment,
            @NonNull List<String> permissions) {
        return hasAlwaysDeniedPermission(new SupportFragmentSource(fragment), permissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment    {@link android.app.Fragment}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull android.app.Fragment fragment,
            @NonNull List<String> permissions) {
        return hasAlwaysDeniedPermission(new FragmentSource(fragment), permissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param context     {@link Context}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull Context context,
            @NonNull List<String> permissions) {
        return hasAlwaysDeniedPermission(new ContextTarget(context), permissions);
    }

    /**
     * Has always been denied permission.
     */
    private static boolean hasAlwaysDeniedPermission(
            @NonNull Source source,
            @NonNull List<String> permissions) {
        for (String permission : permissions) {
            if (!AndPermission.hasPermission(source.getContext(), permissions) && !source.isShowRationalePermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param activity          {@link Activity}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull Activity activity,
            @NonNull String[] deniedPermissions) {
        return hasAlwaysDeniedPermission(new AppActivitySource(activity), deniedPermissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment    {@link android.support.v4.app.Fragment}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull android.support.v4.app.Fragment fragment,
            @NonNull String[] permissions) {
        return hasAlwaysDeniedPermission(new SupportFragmentSource(fragment), permissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment    {@link android.app.Fragment}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull android.app.Fragment fragment,
            @NonNull String[] permissions) {
        return hasAlwaysDeniedPermission(new FragmentSource(fragment), permissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param context     {@link Context}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull Context context,
            @NonNull String[] permissions) {
        return hasAlwaysDeniedPermission(new ContextTarget(context), permissions);
    }

    /**
     * Has always been denied permission.
     */
    private static boolean hasAlwaysDeniedPermission(
            @NonNull Source source,
            @NonNull String[] permissions) {
        for (String permission : permissions) {
            if (!AndPermission.hasPermission(source.getContext(), permissions) && !source.isShowRationalePermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is it always denied permission.
     *
     * @param activity    {@link Activity}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean isAlwaysDeniedPermission(
            @NonNull Activity activity,
            @NonNull String permissions) {
        return isAlwaysDeniedPermission(new AppActivitySource(activity), permissions);
    }

    /**
     * Is it always denied permission.
     *
     * @param fragment    {@link android.support.v4.app.Fragment}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean isAlwaysDeniedPermission(
            @NonNull android.support.v4.app.Fragment fragment,
            @NonNull String permissions) {
        return isAlwaysDeniedPermission(new SupportFragmentSource(fragment), permissions);
    }

    /**
     * Is it always denied permission.
     *
     * @param fragment    {@link android.app.Fragment}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean isAlwaysDeniedPermission(
            @NonNull android.app.Fragment fragment,
            @NonNull String permissions) {
        return isAlwaysDeniedPermission(new FragmentSource(fragment), permissions);
    }

    /**
     * Is it always denied permission.
     *
     * @param context     {@link Context}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean isAlwaysDeniedPermission(
            @NonNull Context context,
            @NonNull String permissions) {
        return isAlwaysDeniedPermission(new ContextTarget(context), permissions);
    }

    /**
     * Has always been denied permission.
     */
    private static boolean isAlwaysDeniedPermission(
            @NonNull Source source,
            @NonNull String permission) {
        return !AndPermission.hasPermission(source.getContext(), permission) && !source.isShowRationalePermission(permission);
    }

    /**
     * Create a service that opens the permission setting page.
     *
     * @param activity {@link Activity}.
     * @return {@link SettingService}.
     */
    @NonNull
    public static SettingService settingService(@NonNull Activity activity) {
        return new SettingExecutor(new AppActivitySource(activity));
    }

    /**
     * Create a service that opens the permission setting page.
     *
     * @param fragment {@link android.support.v4.app.Fragment}.
     * @return {@link SettingService}.
     */
    @NonNull
    public static SettingService settingService(@NonNull android.support.v4.app.Fragment fragment) {
        return new SettingExecutor(new SupportFragmentSource(fragment));
    }

    /**
     * Create a service that opens the permission setting page.
     *
     * @param fragment {@link android.app.Fragment}.
     * @return {@link SettingService}.
     */
    @NonNull
    public static SettingService settingService(@NonNull android.app.Fragment fragment) {
        return new SettingExecutor(new FragmentSource(fragment));
    }

    /**
     * Create a service that opens the permission setting page.
     *
     * @param context {@link android.app.Fragment}.
     * @return {@link SettingService}.
     */
    @NonNull
    public static SettingService settingService(@NonNull Context context) {
        return new SettingExecutor(new ContextTarget(context));
    }

    /**
     * In the Activity.
     *
     * @param activity {@link Activity}.
     * @return {@link Request}.
     */
    @NonNull
    public static Request with(@NonNull Activity activity) {
        return new DefaultRequest(new AppActivitySource(activity));
    }

    /**
     * In the Activity.
     *
     * @param fragment {@link android.support.v4.app.Fragment}.
     * @return {@link Request}.
     */
    @NonNull
    public static Request with(@NonNull android.support.v4.app.Fragment fragment) {
        return new DefaultRequest(new SupportFragmentSource(fragment));
    }

    /**
     * In the Activity.
     *
     * @param fragment {@link android.app.Fragment}.
     * @return {@link Request}.
     */
    @NonNull
    public static Request with(@NonNull android.app.Fragment fragment) {
        return new DefaultRequest(new FragmentSource(fragment));
    }

    /**
     * Anywhere..
     *
     * @param context {@link Context}.
     * @return {@link Request}.
     */
    @NonNull
    public static Request with(@NonNull Context context) {
        return new DefaultRequest(new ContextTarget(context));
    }

    private AndPermission() {
    }

}