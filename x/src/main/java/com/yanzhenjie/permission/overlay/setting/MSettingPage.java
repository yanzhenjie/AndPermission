/*
 * Copyright 2018 Zhenjie Yan
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
package com.yanzhenjie.permission.overlay.setting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.yanzhenjie.permission.source.Source;

/**
 * Created by Zhenjie Yan on 2018/5/30.
 */
public class MSettingPage {

    private static final String MARK = Build.MANUFACTURER.toLowerCase();

    private Source mSource;

    public MSettingPage(Source source) {
        this.mSource = source;
    }

    public void start(int requestCode) {
        Intent intent;
        if (MARK.contains("meizu")) {
            intent = meiZuApi(mSource.getContext());
        } else {
            intent = defaultApi(mSource.getContext());
        }

        try {
            mSource.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            intent = appDetailsApi(mSource.getContext());
            mSource.startActivityForResult(intent, requestCode);
        }
    }

    private static Intent appDetailsApi(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }

    private static Intent defaultApi(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        if (hasActivity(context, intent)) return intent;

        return appDetailsApi(context);
    }

    private static Intent meiZuApi(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.putExtra("packageName", context.getPackageName());
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
        if (hasActivity(context, intent)) return intent;

        return defaultApi(context);
    }

    private static boolean hasActivity(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
}