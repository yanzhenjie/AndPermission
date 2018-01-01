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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.yanzhenjie.permission.source.Source;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * <p>Setting executor.</p>
 * Created by Yan Zhenjie on 2016/12/28.
 */
class SettingExecutor implements SettingService {

    private static final String PACKAGE = "package";
    private static final String MARK = Build.MANUFACTURER.toLowerCase();

    private static final String HUAWEI_MARK = "huawei";
    private static final String HUAWEI_PKG = "com.huawei.systemmanager";
    private static final String HUAWEI_CLASS = "com.huawei.permissionmanager.ui.MainActivity";


    private static final String XIAOMI_MARK = "xiaomi";
    private static final String XIAOMI_ACTION = "miui.intent.action.APP_PERM_EDITOR";
    private static final String XIAOMI_PKG = "com.miui.securitycenter";
    private static final String XIAOMI_MIUI7_CLASS = "com.miui.permcenter.permissions.AppPermissionsEditorActivity";
    private static final String XIAOMI_MIUI8_CLASS = "com.miui.permcenter.permissions.PermissionsEditorActivity";

    private static final String OPPO_MARK = "oppo";
    private static final String OPPO_PKG = "com.coloros.safecenter";
    private static final String OPPO_CLASS = "com.coloros.safecenter.permission.singlepage.PermissionSinglePageActivity";

    private static final String VIVO_MARK = "vivo";
    private static final String VIVO_PKG = "com.iqoo.secure";
    private static final String VIVO_CLASS = "com.iqoo.secure.safeguard.SoftPermissionDetailActivity ";
//    private static final String VIVO_PKG = "com.vivo.permissionmanager";
//    private static final String VIVO_CLASS = "com.vivo.permissionmanager.activity.PurviewTabActivity";

    private static final String MEIZU_MARK = "meizu";
    private static final String MEIZU_PKG = "com.meizu.safe";
    private static final String MEIZU_N_CLASS = "com.meizu.safe.permission.PermissionMainActivity";

    private Source mTarget;

    SettingExecutor(@NonNull Source target) {
        this.mTarget = target;
    }

    @Override
    public void execute() {
        Context context = mTarget.getContext();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PACKAGE, context.getPackageName());
        intent.setData(Uri.fromParts(PACKAGE, context.getPackageName(), null));

        if (HUAWEI_MARK.contains(MARK)) {
            intent.setComponent(new ComponentName(HUAWEI_PKG, HUAWEI_CLASS));
        } else if (XIAOMI_MARK.contains(MARK)) {
            String property = getMIUIProperty();
            if (property.contains("7") || property.contains("6")) {
                intent = new Intent(XIAOMI_ACTION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("extra_pkgname", context.getPackageName());
                intent.setComponent(new ComponentName(XIAOMI_PKG, XIAOMI_MIUI7_CLASS));
            } else if (property.contains("8") || property.contains("9")) {
                intent = new Intent(XIAOMI_ACTION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("extra_pkgname", context.getPackageName());
                intent.setComponent(new ComponentName(XIAOMI_PKG, XIAOMI_MIUI8_CLASS));
            } else {
                // use default.
            }
        } else if (OPPO_MARK.contains(MARK)) {
//            intent = context.getPackageManager().getLaunchIntentForPackage(OPPO_PKG);
        } else if (VIVO_MARK.contains(MARK)) {
            intent.setComponent(new ComponentName(VIVO_PKG, VIVO_CLASS));
        } else if (MEIZU_MARK.contains(MARK)) {
            intent.setComponent(new ComponentName(MEIZU_PKG, MEIZU_N_CLASS));
        }
        try {
            mTarget.startActivity(intent);
        } catch (Exception e) {
            openAppDetailsSetting(mTarget.getContext());
        }
    }

    @Override
    public void cancel() {
    }

    private static void openAppDetailsSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PACKAGE, context.getPackageName());
        intent.setData(Uri.fromParts(PACKAGE, context.getPackageName(), null));
        context.startActivity(intent);
    }

    private static String getMIUIProperty() {
        BufferedReader reader = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop ro.miui.ui.version.name");
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()), 32);
            return reader.readLine();
        } catch (Exception ignored) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ignored) {
                }
            }
        }
        return "";
    }
}