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
package com.yanzhenjie.permission.overlay.setting;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.yanzhenjie.permission.source.Source;

/**
 * Created by YanZhenjie on 2018/5/30.
 */
public class OverlaySettingPage {

    private static final String MARK = Build.MANUFACTURER.toLowerCase();

    private Source mSource;

    public OverlaySettingPage(Source source) {
        this.mSource = source;
    }

    public void start(int requestCode) {
        if (MARK.contains("meizu")) {
            if (!meiZuApi(requestCode) && !defaultApi(requestCode)) {
                appDetailsApi(requestCode);
            }
        } else if (!defaultApi(requestCode)) {
            appDetailsApi(requestCode);
        }
    }

    private boolean meiZuApi(int requestCode) {
        Intent overlayIntent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        overlayIntent.putExtra("packageName", mSource.getContext().getPackageName());
        overlayIntent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity"));
        try {
            mSource.startActivityForResult(overlayIntent, requestCode);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean defaultApi(int requestCode) {
        Intent manageIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        manageIntent.setData(Uri.fromParts("package", mSource.getContext().getPackageName(), null));
        try {
            mSource.startActivityForResult(manageIntent, requestCode);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void appDetailsApi(int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", mSource.getContext().getPackageName(), null));
        mSource.startActivityForResult(intent, requestCode);
    }
}