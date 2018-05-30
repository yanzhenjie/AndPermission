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
package com.yanzhenjie.permission.source;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

/**
 * <p>The source of the request.</p>
 * Created by Yan Zhenjie on 2017/5/1.
 */
public abstract class Source {

    private PackageManager mPackageManager;
    private AppOpsManager mAppOpsManager;

    public abstract Context getContext();

    public abstract void startActivity(Intent intent);

    public abstract void startActivityForResult(Intent intent, int requestCode);

    public abstract boolean isShowRationalePermission(String permission);

    public final boolean canRequestPackageInstalls() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mPackageManager == null) mPackageManager = getContext().getPackageManager();
            return mPackageManager.canRequestPackageInstalls();
        }
        return true;
    }

    public final boolean canDrawOverlays() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Context context = getContext();
            if (!Settings.canDrawOverlays(context)) return false;

            if (mAppOpsManager == null) mAppOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int result = mAppOpsManager.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context.getPackageName());
            if (result != AppOpsManager.MODE_ALLOWED) return false;
        }
        return true;
    }
}