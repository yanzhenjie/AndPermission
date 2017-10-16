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
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.yanzhenjie.permission.target.AppActivityTarget;
import com.yanzhenjie.permission.target.AppFragmentTarget;
import com.yanzhenjie.permission.target.ContextTarget;
import com.yanzhenjie.permission.target.SupportFragmentTarget;

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
            int result = ContextCompat.checkSelfPermission(context, permission);
            if (result == PackageManager.PERMISSION_DENIED) return false;

            String op = AppOpsManagerCompat.permissionToOp(permission);
            if (TextUtils.isEmpty(op)) continue;
            result = AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName());
            if (result != AppOpsManagerCompat.MODE_ALLOWED) return false;

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
    public static boolean hasAlwaysDeniedPermission(@NonNull Activity activity, @NonNull List<String> deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;

        if (deniedPermissions.size() == 0) return false;

        for (String permission : deniedPermissions) {
            boolean rationale = activity.shouldShowRequestPermissionRationale(permission);
            if (!rationale) return true;
        }
        return false;
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment          {@link android.support.v4.app.Fragment}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull android.support.v4.app.Fragment fragment,
            @NonNull List<String> deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;

        if (deniedPermissions.size() == 0) return false;

        for (String permission : deniedPermissions) {
            boolean rationale = fragment.shouldShowRequestPermissionRationale(permission);
            if (!rationale) return true;
        }
        return false;
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment          {@link android.app.Fragment}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull android.app.Fragment fragment,
            @NonNull List<String> deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;

        if (deniedPermissions.size() == 0) return false;

        for (String permission : deniedPermissions) {
            boolean rationale = fragment.shouldShowRequestPermissionRationale(permission);
            if (!rationale) return true;
        }
        return false;
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param context           {@link Context}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull Context context,
            @NonNull List<String> deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;

        if (deniedPermissions.size() == 0) return false;

        if (!(context instanceof Activity)) return false;

        for (String permission : deniedPermissions) {
            boolean rationale = ((Activity) context).shouldShowRequestPermissionRationale(permission);
            if (!rationale) return true;
        }
        return false;
    }

    /**
     * Get default rationale dialog.
     *
     * @param context   {@link Context}.
     * @param rationale {@link Rationale}.
     * @return {@link RationaleDialog}.
     */
    public static
    @NonNull
    RationaleDialog rationaleDialog(@NonNull Context context, Rationale rationale) {
        return new RationaleDialog(context, rationale);
    }

    /**
     * Get default setting dialog.
     *
     * @param activity    {@link Activity}.
     * @param requestCode requestCode for {@code startActivityForResult(Intent, int)}.
     * @return {@link SettingDialog}.
     */
    public static
    @NonNull
    SettingDialog defaultSettingDialog(@NonNull Activity activity, int requestCode) {
        return new SettingDialog(activity, new SettingExecutor(new AppActivityTarget(activity), requestCode));
    }

    /**
     * Get default setting dialog.
     *
     * @param fragment    {@link android.support.v4.app.Fragment}.
     * @param requestCode requestCode for {@code startActivityForResult(Intent, int)}.
     * @return {@link SettingDialog}.
     */
    public static
    @NonNull
    SettingDialog defaultSettingDialog(@NonNull android.support.v4.app.Fragment fragment, int requestCode) {
        return new SettingDialog(fragment.getActivity(), new SettingExecutor(new SupportFragmentTarget(fragment), requestCode));
    }

    /**
     * Get default setting dialog.
     *
     * @param fragment    {@link android.app.Fragment}.
     * @param requestCode requestCode for {@code startActivityForResult(Intent, int)}.
     * @return {@link SettingDialog}.
     */
    public static
    @NonNull
    SettingDialog defaultSettingDialog(@NonNull android.app.Fragment fragment, int requestCode) {
        return new SettingDialog(fragment.getActivity(), new SettingExecutor(new AppFragmentTarget(fragment), requestCode));
    }

    /**
     * Get default setting dialog.
     *
     * @param context {@link Context}.
     * @return {@link SettingDialog}.
     */
    public static
    @NonNull
    SettingDialog defaultSettingDialog(@NonNull Context context) {
        return new SettingDialog(context, new SettingExecutor(new ContextTarget(context), 0));
    }

    /**
     * Get define setting dialog setting object.
     *
     * @param activity    {@link Activity}.
     * @param requestCode requestCode for {@code startActivityForResult(Intent, int)}.
     * @return {@link SettingService}.
     */
    public static
    @NonNull
    SettingService defineSettingDialog(@NonNull Activity activity, int requestCode) {
        return new SettingExecutor(new AppActivityTarget(activity), requestCode);
    }

    /**
     * Get define setting dialog setting object.
     *
     * @param fragment    {@link android.support.v4.app.Fragment}.
     * @param requestCode requestCode for {@code startActivityForResult(Intent, int)}.
     * @return {@link SettingService}.
     */
    public static
    @NonNull
    SettingService defineSettingDialog(@NonNull android.support.v4.app.Fragment fragment, int requestCode) {
        return new SettingExecutor(new SupportFragmentTarget(fragment), requestCode);
    }

    /**
     * Get define setting dialog setting object.
     *
     * @param fragment    {@link android.app.Fragment}.
     * @param requestCode requestCode for {@code startActivityForResult(Intent, int)}.
     * @return {@link SettingService}.
     */
    public static
    @NonNull
    SettingService defineSettingDialog(@NonNull android.app.Fragment fragment, int requestCode) {
        return new SettingExecutor(new AppFragmentTarget(fragment), requestCode);
    }

    /**
     * Get define setting dialog setting object.
     *
     * @param context {@link android.app.Fragment}.
     * @return {@link SettingService}.
     */
    public static
    @NonNull
    SettingService defineSettingDialog(@NonNull Context context) {
        return new SettingExecutor(new ContextTarget(context), 0);
    }

    /**
     * In the Activity.
     *
     * @param activity {@link Activity}.
     * @return {@link Request}.
     */
    public static
    @NonNull
    Request with(@NonNull Activity activity) {
        return new DefaultRequest(new AppActivityTarget(activity));
    }

    /**
     * In the Activity.
     *
     * @param fragment {@link android.support.v4.app.Fragment}.
     * @return {@link Request}.
     */
    public static
    @NonNull
    Request with(@NonNull android.support.v4.app.Fragment fragment) {
        return new DefaultRequest(new SupportFragmentTarget(fragment));
    }

    /**
     * In the Activity.
     *
     * @param fragment {@link android.app.Fragment}.
     * @return {@link Request}.
     */
    public static
    @NonNull
    Request with(@NonNull android.app.Fragment fragment) {
        return new DefaultRequest(new AppFragmentTarget(fragment));
    }

    /**
     * Anywhere..
     *
     * @param context {@link Context}.
     * @return {@link Request}.
     */
    public static
    @NonNull
    Request with(@NonNull Context context) {
        return new DefaultRequest(new ContextTarget(context));
    }

    private AndPermission() {
    }

}
