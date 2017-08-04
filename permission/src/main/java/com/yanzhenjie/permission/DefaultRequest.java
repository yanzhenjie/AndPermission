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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.yanzhenjie.permission.target.Target;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Request permission and callback.</p>
 * Created by Yan Zhenjie on 2016/9/9.
 */
class DefaultRequest implements
        Request,
        Rationale,
        PermissionActivity.RationaleListener,
        PermissionActivity.PermissionListener {

    private static final String TAG = "AndPermission";

    private Target target;

    private int mRequestCode;
    private String[] mPermissions;
    private Object mCallback;
    private RationaleListener mRationaleListener;

    private String[] mDeniedPermissions;

    DefaultRequest(Target target) {
        if (target == null)
            throw new IllegalArgumentException("The target can not be null.");
        this.target = target;
    }

    @NonNull
    @Override
    public Request permission(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    @NonNull
    @Override
    public Request permission(String[]... permissionsArray) {
        List<String> permissionList = new ArrayList<>();
        for (String[] permissions : permissionsArray) {
            for (String permission : permissions) {
                permissionList.add(permission);
            }
        }
        this.mPermissions = permissionList.toArray(new String[permissionList.size()]);
        return this;
    }

    @NonNull
    @Override
    public Request requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    @Override
    public Request callback(Object callback) {
        this.mCallback = callback;
        return this;
    }

    @NonNull
    @Override
    public Request rationale(RationaleListener listener) {
        this.mRationaleListener = listener;
        return this;
    }

    @Deprecated
    @Override
    public void send() {
        start();
    }

    @Override
    public void start() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callbackSucceed();
        } else {
            mDeniedPermissions = getDeniedPermissions(target.getContext(), mPermissions);
            // Denied mPermissions size > 0.
            if (mDeniedPermissions.length > 0) {
                // Remind users of the purpose of mPermissions.
                PermissionActivity.setRationaleListener(this);
                Intent intent = new Intent(target.getContext(), PermissionActivity.class);
                intent.putExtra(PermissionActivity.KEY_INPUT_PERMISSIONS, mDeniedPermissions);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                target.startActivity(intent);
            } else { // All permission granted.
                callbackSucceed();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static String[] getDeniedPermissions(Context context, @NonNull String... permissions) {
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                deniedList.add(permission);
        return deniedList.toArray(new String[deniedList.size()]);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRationaleResult(boolean showRationale) {
        if (showRationale && mRationaleListener != null) {
            mRationaleListener.showRequestPermissionRationale(mRequestCode, this);
        } else {
            resume();
        }
    }

    @Override
    public void cancel() {
        int[] results = new int[mPermissions.length];
        for (int i = 0; i < mPermissions.length; i++)
            results[i] = ContextCompat.checkSelfPermission(target.getContext(), mPermissions[i]);
        onRequestPermissionsResult(mPermissions, results);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void resume() {
        PermissionActivity.setPermissionListener(this);
        Intent intent = new Intent(target.getContext(), PermissionActivity.class);
        intent.putExtra(PermissionActivity.KEY_INPUT_PERMISSIONS, mDeniedPermissions);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        target.startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> deniedList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++)
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                deniedList.add(permissions[i]);

        if (deniedList.isEmpty())
            callbackSucceed();
        else
            callbackFailed(deniedList);
    }

    private void callbackSucceed() {
        if (mCallback != null) {
            if (mCallback instanceof PermissionListener)
                ((PermissionListener) mCallback).onSucceed(mRequestCode, Arrays.asList(mPermissions));
            else {
                callbackAnnotation(mCallback, mRequestCode, PermissionYes.class, Arrays.asList(mPermissions));
            }
        }
    }

    private void callbackFailed(List<String> deniedList) {
        if (mCallback != null) {
            if (mCallback instanceof PermissionListener)
                ((PermissionListener) mCallback).onFailed(mRequestCode, deniedList);
            else {
                callbackAnnotation(mCallback, mRequestCode, PermissionNo.class, deniedList);
            }
        }
    }

    private static void callbackAnnotation(
            Object callback,
            int requestCode,
            Class<? extends Annotation> annotationClass,
            List<String> permissions) {
        Method[] methods = findMethodForRequestCode(callback.getClass(), annotationClass, requestCode);
        if (methods.length == 0)
            Log.e(TAG, "Do you forget @PermissionYes or @PermissionNo for callback method ?");
        else
            try {
                for (Method method : methods) {
                    if (!method.isAccessible()) method.setAccessible(true);
                    method.invoke(callback, permissions);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private static Method[] findMethodForRequestCode(
            @NonNull Class<?> source,
            @NonNull Class<? extends Annotation> annotation,
            int requestCode) {
        List<Method> methods = new ArrayList<>(1);
        for (Method method : source.getDeclaredMethods())
            if (method.getAnnotation(annotation) != null)
                if (isSameRequestCode(method, annotation, requestCode))
                    methods.add(method);
        return methods.toArray(new Method[methods.size()]);
    }

    private static boolean isSameRequestCode(
            @NonNull Method method,
            @NonNull Class<? extends Annotation> annotation,
            int requestCode) {
        if (PermissionYes.class.equals(annotation))
            return method.getAnnotation(PermissionYes.class).value() == requestCode;
        else if (PermissionNo.class.equals(annotation))
            return method.getAnnotation(PermissionNo.class).value() == requestCode;
        return false;
    }
}