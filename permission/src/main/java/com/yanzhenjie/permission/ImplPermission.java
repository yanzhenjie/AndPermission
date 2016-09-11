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
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by Yan Zhenjie on 2016/9/9.
 */
class ImplPermission implements Permission {

    private String[] permissions;
    private String[] deniedPermissions;
    private int requestCode;
    private Object object;
    private RationaleListener listener;

    ImplPermission(Object o) {
        if (o == null)
            throw new IllegalArgumentException("The argument can not be null.");
        this.object = o;
    }

    @Override
    public Permission permission(String... permissions) {
        if (permissions == null)
            throw new IllegalArgumentException("The permissions can not be null.");
        this.permissions = permissions;
        return this;
    }

    @Override
    public Permission requestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    @Override
    public Permission rationale(RationaleListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void send() {
        if (permissions.length == 0) {
            invokeOnRequestPermissionsResult(object, requestCode, permissions, new int[0]);
        } else if (object instanceof Activity || object instanceof Fragment) {
            if (Build.VERSION.SDK_INT >= 23) {
                deniedPermissions = PermissionUtils.getDeniedPermissions(object, permissions);
                if (deniedPermissions.length > 0) { // Denied permissions size > 0.
                    final String[] rationalePermissions = PermissionUtils.getShouldShowRationalePermissions(object, deniedPermissions);
                    if (rationalePermissions.length > 0 && listener != null) // Remind users of the purpose of permissions.
                        listener.showRequestPermissionRationale(requestCode, rationale);
                    else
                        invokeRequestPermissions(object, requestCode, deniedPermissions);
                } else { // All permission granted.
                    int[] grantResults = new int[permissions.length];
                    for (int i = 0; i < grantResults.length; i++)
                        grantResults[i] = PackageManager.PERMISSION_GRANTED;
                    invokeOnRequestPermissionsResult(object, requestCode, permissions, grantResults);
                }
            } else // Check all permission result and dispatch.
                invokeOnRequestPermissionsResult(object, requestCode, permissions, PermissionUtils.getPermissionsResults(object, permissions));
        } else {
            Log.w("AndPermission", "The " + object.getClass().getName() + " is not support");
        }
    }

    private Rationale rationale = new Rationale() {
        @Override
        public void cancel() {
            invokeOnRequestPermissionsResult(object, requestCode, permissions, PermissionUtils.getPermissionsResults(object, permissions));
        }

        @Override
        public void resume() {
            invokeRequestPermissions(object, requestCode, deniedPermissions);
        }
    };

    @TargetApi(23)
    private static void invokeRequestPermissions(Object o, int requestCode, String... permissions) {
        if (o instanceof Activity)
            ((Activity) o).requestPermissions(permissions, requestCode);
        else if (o instanceof Fragment)
            ((Fragment) o).requestPermissions(permissions, requestCode);
    }

    private static void invokeOnRequestPermissionsResult(Object o, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (o instanceof Activity) {
            if (Build.VERSION.SDK_INT >= 23)
                ((Activity) o).onRequestPermissionsResult(requestCode, permissions, grantResults);
            else if (o instanceof ActivityCompat.OnRequestPermissionsResultCallback)
                ((ActivityCompat.OnRequestPermissionsResultCallback) o).onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (o instanceof Fragment) {
            ((Fragment) o).onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
