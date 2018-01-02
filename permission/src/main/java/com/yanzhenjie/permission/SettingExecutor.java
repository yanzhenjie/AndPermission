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

/**
 * <p>Setting executor.</p>
 * Created by Yan Zhenjie on 2016/12/28.
 */
class SettingExecutor implements SettingService {

    private static final String DEFAULT_PACKAGE_KEY = "package";
    private static final String MARK = Build.MANUFACTURER.toLowerCase();

    private static final String HUAWEI_MARK = "huawei";
    private static final String HUAWEI_PKG = "com.huawei.systemmanager";
    private static final String HUAWEI_CLASS = "com.huawei.permissionmanager.ui.MainActivity";

    private static final String XIAOMI_MARK = "xiaomi";
    private static final String XIAOMI_ACTION = "miui.intent.action.APP_PERM_EDITOR";
    private static final String XIAOMI_PACKAGE_KEY = "extra_pkgname";

    private static final String OPPO_MARK = "oppo";
    private static final String OPPO_PKG = "com.coloros.safecenter";
    private static final String OPPO_CLASS = "com.coloros.safecenter.permission.PermissionManagerActivity";

    private static final String VIVO_MARK = "vivo";
    private static final String VIVO_PACKAGE_KEY = "packagename";
    private static final String VIVO_PKG = "com.iqoo.secure";
    private static final String VIVO_CLASS = "com.iqoo.secure.safeguard.SoftPermissionDetailActivity";

    private static final String MEIZU_MARK = "meizu";
    private static final String MEIZU_ACTION = "com.meizu.safe.security.SHOW_APPSEC";
    private static final String MEIZU_PACKAGE_KEY = "packageName";
    private static final String MEIZU_PKG = "com.meizu.safe";
    private static final String MEIZU_N_CLASS = "com.meizu.safe.security.AppSecActivity";

    private Source mTarget;

    SettingExecutor(@NonNull Source target) {
        this.mTarget = target;
    }

    @Override
    public void execute() {
        Context context = mTarget.getContext();
        Intent intent = null;
        if (HUAWEI_MARK.contains(MARK)) {
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.fromParts(DEFAULT_PACKAGE_KEY, context.getPackageName(), null));
            intent.putExtra(DEFAULT_PACKAGE_KEY, context.getPackageName());
            intent.setComponent(new ComponentName(HUAWEI_PKG, HUAWEI_CLASS));
        } else if (XIAOMI_MARK.contains(MARK)) {
            intent = new Intent(XIAOMI_ACTION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(XIAOMI_PACKAGE_KEY, context.getPackageName());
        } else if (OPPO_MARK.contains(MARK)) {
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DEFAULT_PACKAGE_KEY, context.getPackageName());
            intent.setComponent(new ComponentName(OPPO_PKG, OPPO_CLASS));
        } else if (VIVO_MARK.contains(MARK)) {
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(VIVO_PACKAGE_KEY, context.getPackageName());
            intent.setComponent(new ComponentName(VIVO_PKG, VIVO_CLASS));
        } else if (MEIZU_MARK.contains(MARK)) {
            intent = new Intent(MEIZU_ACTION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(MEIZU_PACKAGE_KEY, context.getPackageName());
            intent.setComponent(new ComponentName(MEIZU_PKG, MEIZU_N_CLASS));
        }
        if (intent == null) {
            openAppDetailsSetting(mTarget.getContext());
        } else {
            try {
                mTarget.startActivity(intent);
            } catch (Exception e) {
                openAppDetailsSetting(mTarget.getContext());
            }
        }
    }

    @Override
    public void cancel() {
    }

    private static void openAppDetailsSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromParts(DEFAULT_PACKAGE_KEY, context.getPackageName(), null));
        context.startActivity(intent);
    }
}