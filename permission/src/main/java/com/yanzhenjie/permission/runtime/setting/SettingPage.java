/*
 * Copyright 2018 Yan Zhenjie
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
package com.yanzhenjie.permission.runtime.setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.yanzhenjie.permission.source.Source;

/**
 * Created by YanZhenjie on 2018/4/30.
 */
public class SettingPage {

    private static final String MARK = Build.MANUFACTURER.toLowerCase();

    private Source mSource;

    public SettingPage(Source source) {
        this.mSource = source;
    }

    /**
     * Start.
     *
     * @param requestCode this code will be returned in onActivityResult() when the activity exits.
     */
    public void start(int requestCode) {
        Intent intent;
        if (MARK.contains("huawei")) {
            intent = huaweiApi(mSource.getContext());
        } else if (MARK.contains("xiaomi")) {
            intent = xiaomiApi(mSource.getContext());
        } else if (MARK.contains("oppo")) {
            intent = oppoApi(mSource.getContext());
        } else if (MARK.contains("vivo")) {
            intent = vivoApi(mSource.getContext());
        } else if (MARK.contains("samsung")) {
            intent = samsungApi(mSource.getContext());
        } else if (MARK.contains("meizu")) {
            intent = meizuApi(mSource.getContext());
        } else if (MARK.contains("smartisan")) {
            intent = smartisanApi(mSource.getContext());
        } else {
            intent = defaultApi(mSource.getContext());
        }
        try {
            mSource.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            intent = defaultApi(mSource.getContext());
            mSource.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * App details page.
     */
    private static Intent defaultApi(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }

    /**
     * Huawei cell phone Api23 the following method.
     */
    private static Intent huaweiApi(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return defaultApi(context);
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"));
        return intent;
    }

    /**
     * Xiaomi phone to achieve the method.
     */
    private static Intent xiaomiApi(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.putExtra("extra_pkgname", context.getPackageName());
        return intent;
    }

    /**
     * Vivo phone to achieve the method.
     */
    private static Intent vivoApi(Context context) {
        Intent intent = new Intent();
        intent.putExtra("packagename", context.getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity"));
        } else {
            intent.setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.safeguard.SoftPermissionDetailActivity"));
        }
        return intent;
    }

    /**
     * Oppo phone to achieve the method.
     */
    private static Intent oppoApi(Context context) {
        return defaultApi(context);
    }

    /**
     * Meizu phone to achieve the method.
     */
    private static Intent meizuApi(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return defaultApi(context);
        }
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.putExtra("packageName", context.getPackageName());
        intent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity"));
        return intent;
    }

    /**
     * Smartisan phone to achieve the method.
     */
    private static Intent smartisanApi(Context context) {
        return defaultApi(context);
    }

    /**
     * Samsung phone to achieve the method.
     */
    private static Intent samsungApi(Context context) {
        return defaultApi(context);
    }
}