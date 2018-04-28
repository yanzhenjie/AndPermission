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
import android.net.Uri;
import android.os.Build;

import com.yanzhenjie.permission.setting.PermissionSetting;
import com.yanzhenjie.permission.source.AppActivitySource;
import com.yanzhenjie.permission.source.ContextSource;
import com.yanzhenjie.permission.source.FragmentSource;
import com.yanzhenjie.permission.source.Source;
import com.yanzhenjie.permission.source.SupportFragmentSource;

import java.io.File;
import java.util.List;

/**
 * <p>Request the entrance.</p>
 * Created by Yan Zhenjie on 2016/9/9.
 */
public class AndPermission {

    private static final RequestFactory FACTORY;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FACTORY = new MRequestFactory();
        } else {
            FACTORY = new LRequestFactory();
        }
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param activity          {@link Activity}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(Activity activity, List<String> deniedPermissions) {
        return hasAlwaysDeniedPermission(new AppActivitySource(activity), deniedPermissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment          {@link android.support.v4.app.Fragment}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(android.support.v4.app.Fragment fragment, List<String> deniedPermissions) {
        return hasAlwaysDeniedPermission(new SupportFragmentSource(fragment), deniedPermissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment          {@link android.app.Fragment}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(android.app.Fragment fragment, List<String> deniedPermissions) {
        return hasAlwaysDeniedPermission(new FragmentSource(fragment), deniedPermissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param context           {@link Context}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(Context context, List<String> deniedPermissions) {
        return hasAlwaysDeniedPermission(new ContextSource(context), deniedPermissions);
    }

    /**
     * Has always been denied permission.
     */
    private static boolean hasAlwaysDeniedPermission(Source source, List<String> deniedPermissions) {
        for (String permission : deniedPermissions) {
            if (!source.isShowRationalePermission(permission)) {
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
    public static boolean hasAlwaysDeniedPermission(Activity activity, String... deniedPermissions) {
        return hasAlwaysDeniedPermission(new AppActivitySource(activity), deniedPermissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment          {@link android.support.v4.app.Fragment}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(android.support.v4.app.Fragment fragment, String... deniedPermissions) {
        return hasAlwaysDeniedPermission(new SupportFragmentSource(fragment), deniedPermissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment          {@link android.app.Fragment}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(android.app.Fragment fragment, String... deniedPermissions) {
        return hasAlwaysDeniedPermission(new FragmentSource(fragment), deniedPermissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param context           {@link Context}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(Context context, String... deniedPermissions) {
        return hasAlwaysDeniedPermission(new ContextSource(context), deniedPermissions);
    }

    /**
     * Has always been denied permission.
     */
    private static boolean hasAlwaysDeniedPermission(Source source, String... deniedPermissions) {
        for (String permission : deniedPermissions) {
            if (!source.isShowRationalePermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create a service that opens the permission setting page.
     *
     * @param activity {@link Activity}.
     * @return {@link SettingService}.
     */
    public static SettingService permissionSetting(Activity activity) {
        return new PermissionSetting(new AppActivitySource(activity));
    }

    /**
     * Create a service that opens the permission setting page.
     *
     * @param fragment {@link android.support.v4.app.Fragment}.
     * @return {@link SettingService}.
     */
    public static SettingService permissionSetting(android.support.v4.app.Fragment fragment) {
        return new PermissionSetting(new SupportFragmentSource(fragment));
    }

    /**
     * Create a service that opens the permission setting page.
     *
     * @param fragment {@link android.app.Fragment}.
     * @return {@link SettingService}.
     */
    public static SettingService permissionSetting(android.app.Fragment fragment) {
        return new PermissionSetting(new FragmentSource(fragment));
    }

    /**
     * Create a service that opens the permission setting page.
     *
     * @param context {@link Context}.
     * @return {@link SettingService}.
     */
    public static SettingService permissionSetting(Context context) {
        return new PermissionSetting(new ContextSource(context));
    }

    /**
     * With Activity.
     *
     * @param activity {@link Activity}.
     * @return {@link Request}.
     */
    public static Request with(Activity activity) {
        return FACTORY.create(new AppActivitySource(activity));
    }

    /**
     * With android.support.v4.app.Fragment.
     *
     * @param fragment {@link android.support.v4.app.Fragment}.
     * @return {@link Request}.
     */
    public static Request with(android.support.v4.app.Fragment fragment) {
        return FACTORY.create(new SupportFragmentSource(fragment));
    }

    /**
     * With android.app.Fragment.
     *
     * @param fragment {@link android.app.Fragment}.
     * @return {@link Request}.
     */
    public static Request with(android.app.Fragment fragment) {
        return FACTORY.create(new FragmentSource(fragment));
    }

    /**
     * With context.
     *
     * @param context {@link Context}.
     * @return {@link Request}.
     */
    public static Request with(Context context) {
        return FACTORY.create(new ContextSource(context));
    }

    private AndPermission() {
    }

    private interface RequestFactory {
        /**
         * Create permission request.
         */
        Request create(Source source);
    }

    private static class LRequestFactory implements RequestFactory {
        @Override
        public Request create(Source source) {
            return new LRequest(source);
        }
    }

    private static class MRequestFactory implements RequestFactory {
        @Override
        public Request create(Source source) {
            return new MRequest(source);
        }
    }

    /**
     * Get compatible Android 7.0 and lower versions of Uri.
     *
     * @param context context.
     * @param file    file.
     * @return uri.
     */
    public static Uri getFileUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".file.path.share", file);
        }
        return Uri.fromFile(file);
    }
}