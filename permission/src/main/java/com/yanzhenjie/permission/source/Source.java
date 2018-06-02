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

import java.lang.reflect.Method;

/**
 * <p>The source of the request.</p>
 * Created by Yan Zhenjie on 2017/5/1.
 */
public abstract class Source {

    private static final String OPSTR_SYSTEM_ALERT_WINDOW = AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW;
    private static final int OP_REQUEST_INSTALL_PACKAGES = 66;

    private PackageManager mPackageManager;
    private AppOpsManager mAppOpsManager;
    private int mTargetSdkVersion;

    public abstract Context getContext();

    public abstract void startActivity(Intent intent);

    public abstract void startActivityForResult(Intent intent, int requestCode);

    public abstract boolean isShowRationalePermission(String permission);

    private int getTargetSdkVersion() {
        if (mTargetSdkVersion < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mTargetSdkVersion = getContext().getApplicationInfo().targetSdkVersion;
        }
        return mTargetSdkVersion;
    }

    private PackageManager getPackageManager() {
        if (mPackageManager == null) {
            mPackageManager = getContext().getPackageManager();
        }
        return mPackageManager;
    }

    private AppOpsManager getAppOpsManager() {
        if (mAppOpsManager == null) {
            mAppOpsManager = (AppOpsManager) getContext().getSystemService(Context.APP_OPS_SERVICE);
        }
        return mAppOpsManager;
    }

    public final boolean canRequestPackageInstalls() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (getTargetSdkVersion() < Build.VERSION_CODES.O) {
                Class<AppOpsManager> clazz = AppOpsManager.class;
                try {
                    Method method = clazz.getDeclaredMethod("checkOpNoThrow", int.class, int.class, String.class);
                    int result = (int) method.invoke(getAppOpsManager(), OP_REQUEST_INSTALL_PACKAGES, android.os.Process.myUid(), getContext().getPackageName());
                    return result == AppOpsManager.MODE_ALLOWED;
                } catch (Exception ignored) {
                    // Android P does not allow reflections.
                    return true;
                }
            }
            return getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    public final boolean canDrawOverlays() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Context context = getContext();
            // Not available when targetSdkVersion is lower than M.
//            if (getTargetSdkVersion() >= Build.VERSION_CODES.M) {
//                return Settings.canDrawOverlays(context);
//            }

            int result = getAppOpsManager().checkOpNoThrow(OPSTR_SYSTEM_ALERT_WINDOW, android.os.Process.myUid(), context.getPackageName());
            return result == AppOpsManager.MODE_ALLOWED;
        }
        return true;
    }
}