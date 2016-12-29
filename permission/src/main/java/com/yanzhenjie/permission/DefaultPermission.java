/*
 * Copyright 2016 Yan Zhenjie
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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yan Zhenjie on 2016/9/9.
 */
class DefaultPermission implements Permission {

    private static final String TAG = "AndPermission";

    private String[] permissions;
    private String[] deniedPermissions;
    private int requestCode;
    private Object object;
    private RationaleListener listener;

    DefaultPermission(Object o) {
        if (o == null)
            throw new IllegalArgumentException("The object can not be null.");
        this.object = o;
    }

    @NonNull
    @Override
    public Permission permission(String... permissions) {
        if (permissions == null)
            throw new IllegalArgumentException("The permissions can not be null.");
        this.permissions = permissions;
        return this;
    }

    @NonNull
    @Override
    public Permission requestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    @NonNull
    @Override
    public Permission rationale(RationaleListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Rationale.
     */
    private Rationale rationale = new Rationale() {
        @Override
        public void cancel() {
            int[] results = new int[permissions.length];
            Context context = PermissionUtils.getContext(object);
            for (int i = 0; i < results.length; i++) {
                results[i] = ActivityCompat.checkSelfPermission(context, permissions[i]);
            }
            onRequestPermissionsResult(object, requestCode, permissions, results);
        }

        @Override
        public void resume() {
            requestPermissions(object, requestCode, deniedPermissions);
        }
    };

    @Override
    public void send() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Context context = PermissionUtils.getContext(object);

            final int[] grantResults = new int[permissions.length];
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();

            final int permissionCount = permissions.length;
            for (int i = 0; i < permissionCount; i++) {
                grantResults[i] = packageManager.checkPermission(permissions[i], packageName);
            }
            onRequestPermissionsResult(object, requestCode, permissions, grantResults);
        } else {
            deniedPermissions = getDeniedPermissions(object, permissions);
            // Denied permissions size > 0.
            if (deniedPermissions.length > 0) {
                // Remind users of the purpose of permissions.
                boolean showRationale = PermissionUtils.shouldShowRationalePermissions(object, deniedPermissions);
                if (showRationale && listener != null)
                    listener.showRequestPermissionRationale(requestCode, rationale);
                else
                    rationale.resume();
            } else { // All permission granted.
                final int[] grantResults = new int[permissions.length];

                final int permissionCount = permissions.length;
                for (int i = 0; i < permissionCount; i++) {
                    grantResults[i] = PackageManager.PERMISSION_GRANTED;
                }
                onRequestPermissionsResult(object, requestCode, permissions, grantResults);
            }
        }
    }

    private static String[] getDeniedPermissions(Object o, String... permissions) {
        Context context = PermissionUtils.getContext(o);
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions)
            if (!AndPermission.hasPermission(context, permission))
                deniedList.add(permission);
        return deniedList.toArray(new String[deniedList.size()]);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void requestPermissions(Object o, int requestCode, String... permissions) {
        if (o instanceof Activity)
            ActivityCompat.requestPermissions(((Activity) o), permissions, requestCode);
        else if (o instanceof android.support.v4.app.Fragment)
            ((android.support.v4.app.Fragment) o).requestPermissions(permissions, requestCode);
        else if (o instanceof android.app.Fragment) {
            ((android.app.Fragment) o).requestPermissions(permissions, requestCode);
            Log.e(TAG, "The " + o.getClass().getName() + " is not support " + "requestPermissions()");
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void onRequestPermissionsResult(Object o, int requestCode, @NonNull String[] permissions,
                                                   @NonNull int[] grantResults) {
        if (o instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                ((Activity) o).onRequestPermissionsResult(requestCode, permissions, grantResults);
            else if (o instanceof ActivityCompat.OnRequestPermissionsResultCallback)
                ((ActivityCompat.OnRequestPermissionsResultCallback) o).onRequestPermissionsResult(requestCode,
                        permissions, grantResults);
            else
                Log.e(TAG, "The " + o.getClass().getName() + " is not support " + "onRequestPermissionsResult()");
        } else if (o instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) o).onRequestPermissionsResult(requestCode, permissions,
                    grantResults);
        } else if (o instanceof android.app.Fragment) {
            ((android.app.Fragment) o).onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
