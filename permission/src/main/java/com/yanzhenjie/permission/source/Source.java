/*
 * Copyright © Zhenjie Yan
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
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>The source of the request.</p>
 * Created by Zhenjie Yan on 2017/5/1.
 */
public abstract class Source {

    private static final int MODE_ASK = 4;
    private static final int MODE_COMPAT = 5;

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_REQUEST_INSTALL_PACKAGES = "OP_REQUEST_INSTALL_PACKAGES";
    private static final String OP_SYSTEM_ALERT_WINDOW = "OP_SYSTEM_ALERT_WINDOW";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static final String OP_ACCESS_NOTIFICATIONS = "OP_ACCESS_NOTIFICATIONS";
    private static final String OP_WRITE_SETTINGS = "OP_WRITE_SETTINGS";

    private int mTargetSdkVersion;
    private String mPackageName;
    private PackageManager mPackageManager;
    private AppOpsManager mAppOpsManager;
    private NotificationManager mNotificationManager;

    public abstract Context getContext();

    public abstract void startActivity(Intent intent);

    public abstract void startActivityForResult(Intent intent, int requestCode);

    public abstract boolean isShowRationalePermission(String permission);

    public int getTargetSdkVersion() {
        if (mTargetSdkVersion < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mTargetSdkVersion = getContext().getApplicationInfo().targetSdkVersion;
        }
        return mTargetSdkVersion;
    }

    public String getPackageName() {
        if (mPackageName == null) {
            mPackageName = getContext().getApplicationContext().getPackageName();
        }
        return mPackageName;
    }

    private PackageManager getPackageManager() {
        if (mPackageManager == null) {
            mPackageManager = getContext().getPackageManager();
        }
        return mPackageManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private AppOpsManager getAppOpsManager() {
        if (mAppOpsManager == null) {
            mAppOpsManager = (AppOpsManager) getContext().getSystemService(Context.APP_OPS_SERVICE);
        }
        return mAppOpsManager;
    }

    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    public final boolean canRequestPackageInstalls() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (getTargetSdkVersion() < Build.VERSION_CODES.O) {
                return reflectionOps(OP_REQUEST_INSTALL_PACKAGES);
            }
            return getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    public final boolean canDrawOverlays() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Context context = getContext();
            if (getTargetSdkVersion() >= Build.VERSION_CODES.M) {
                return Settings.canDrawOverlays(context);
            }

            return reflectionOps(OP_SYSTEM_ALERT_WINDOW);
        }
        return true;
    }

    public final boolean canNotify() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getNotificationManager().areNotificationsEnabled();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return reflectionOps(OP_POST_NOTIFICATION);
        } else {
            return true;
        }
    }

    public final boolean canListenerNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return reflectionOps(OP_ACCESS_NOTIFICATIONS);
        }

        Context context = getContext();
        String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        return flat != null && flat.contains(getPackageName());
    }

    public final boolean canWriteSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Context context = getContext();
            if (getTargetSdkVersion() >= Build.VERSION_CODES.M) {
                return Settings.System.canWrite(context);
            }

            return reflectionOps(OP_WRITE_SETTINGS);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean reflectionOps(String opFieldName) {
        int uid = getContext().getApplicationInfo().uid;
        try {
            Class<AppOpsManager> appOpsClass = AppOpsManager.class;
            Method method = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opField = appOpsClass.getDeclaredField(opFieldName);
            int opValue = (int) opField.get(Integer.class);
            int result = (int) method.invoke(getAppOpsManager(), opValue, uid, getPackageName());
            return result == AppOpsManager.MODE_ALLOWED || result == MODE_ASK || result == MODE_COMPAT;
        } catch (Throwable e) {
            return true;
        }
    }
}