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

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.yanzhenjie.permission.source.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * <p>Request permission and callback.</p>
 * Created by Yan Zhenjie on 2016/9/9.
 */
class DefaultRequest implements Request, RequestExecutor, PermissionActivity.PermissionListener {

    private static final String TAG = "AndPermission";

    private Source mSource;

    private String[] mPermissions;
    private RationaleListener mRationaleListener;
    private Action mGranted;
    private Action mDenied;

    private String[] mDeniedPermissions;

    DefaultRequest(Source source) {
        this.mSource = source;
    }

    @NonNull
    @Override
    public Request permission(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    @NonNull
    @Override
    public Request permission(String[]... groups) {
        List<String> permissionList = new ArrayList<>();
        for (String[] group : groups) {
            permissionList.addAll(Arrays.asList(group));
        }
        this.mPermissions = permissionList.toArray(new String[permissionList.size()]);
        return this;
    }


    @NonNull
    @Override
    public Request rationale(RationaleListener listener) {
        this.mRationaleListener = listener;
        return this;
    }

    @NonNull
    @Override
    public Request onGranted(Action granted) {
        this.mGranted = granted;
        return this;
    }

    @NonNull
    @Override
    public Request onDenied(Action denied) {
        this.mDenied = denied;
        return this;
    }

    @Override
    public void start() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onRequestPermissionsResult(mPermissions);
        } else {
            List<String> deniedList = getDeniedPermissions(mSource, mPermissions);
            mDeniedPermissions = deniedList.toArray(new String[deniedList.size()]);
            if (mDeniedPermissions.length > 0) {
                List<String> rationaleList = getRationalePermissions(mSource, mDeniedPermissions);
                if (rationaleList.size() > 0 && mRationaleListener != null) {
                    mRationaleListener.showRationale(mSource.getContext(), rationaleList, this);
                } else {
                    execute();
                }
            } else {
                callbackSucceed();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(@NonNull String[] permissions) {
        List<String> deniedList = getDeniedPermissions(mSource, permissions);
        if (deniedList.isEmpty())
            callbackSucceed();
        else
            callbackFailed(deniedList);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void execute() {
        PermissionActivity.requestPermission(mSource.getContext(), mDeniedPermissions, this);
    }

    @Override
    public void cancel() {
        onRequestPermissionsResult(mDeniedPermissions);
    }

    /**
     * Callback acceptance status.
     */
    private void callbackSucceed() {
        if (mGranted != null) {
            mGranted.onAction(asList(mPermissions));
        }
    }

    /**
     * Callback rejected state.
     */
    private void callbackFailed(@NonNull List<String> deniedList) {
        if (mDenied != null) {
            mDenied.onAction(deniedList);
        }
    }

    /**
     * Get denied permissions.
     */
    private static List<String> getDeniedPermissions(@NonNull Source source, @NonNull String... permissions) {
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (!AndPermission.hasPermission(source.getContext(), permission)) {
                deniedList.add(permission);
            }
        }
        return deniedList;
    }

    /**
     * Get permissions to show rationale.
     */
    private static List<String> getRationalePermissions(@NonNull Source source, @NonNull String... permissions) {
        List<String> rationaleList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (source.isShowRationalePermission(permission)) {
                rationaleList.add(permission);
            }
        }
        return rationaleList;
    }
}