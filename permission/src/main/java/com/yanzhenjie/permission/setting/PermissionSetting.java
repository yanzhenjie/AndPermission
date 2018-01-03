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
package com.yanzhenjie.permission.setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.yanzhenjie.permission.SettingService;
import com.yanzhenjie.permission.source.Source;

/**
 * <p>Setting executor.</p>
 * Created by Yan Zhenjie on 2016/12/28.
 */
public class PermissionSetting implements SettingService {

    private static final String DEFAULT_PACKAGE_KEY = "package";
    private static final String MARK = Build.MANUFACTURER.toLowerCase();

    private Context mContext;

    public PermissionSetting(@NonNull Source source) {
        this.mContext = source.getContext();
    }

    @Override
    public void execute() {
        Intent intent;
        if (MARK.contains("huawei")) {
            intent = huaweiApi(mContext);
        } else if (MARK.contains("xiaomi")) {
            intent = xiaomiApi(mContext);
        } else if (MARK.contains("oppo")) {
            intent = oppoApi(mContext);
        } else if (MARK.contains("vivo")) {
            intent = vivoApi(mContext);
        } else if (MARK.contains("samsung")) {
            intent = samsungApi(mContext);
        } else if (MARK.contains("meizu")) {
            intent = meizuApi(mContext);
        } else if (MARK.contains("smartisan")) {
            intent = smartisanApi(mContext);
        } else {
            intent = defaultApi(mContext);
        }
        try {
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mContext, "发生异常，打开默认页面", Toast.LENGTH_LONG).show();
            mContext.startActivity(defaultApi(mContext));
        }
    }

    @Override
    public void cancel() {
    }

    /**
     * App details page.
     */
    private static Intent defaultApi(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromParts(DEFAULT_PACKAGE_KEY, context.getPackageName(), null));
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"));
        return intent;
    }

    /**
     * Xiaomi phone to achieve the method.
     */
    private static Intent xiaomiApi(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("extra_pkgname", context.getPackageName());
        return intent;
    }

    /**
     * Vivo phone to achieve the method.
     */
    private static Intent vivoApi(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packagename", context.getPackageName());
        intent.setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.safeguard.SoftPermissionDetailActivity"));
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
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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