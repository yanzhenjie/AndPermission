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
package com.yanzhenjie.permission.runtime;

import android.content.Context;
import android.util.Log;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.PermissionActivity;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.checker.DoubleChecker;
import com.yanzhenjie.permission.checker.PermissionChecker;
import com.yanzhenjie.permission.checker.StandardChecker;
import com.yanzhenjie.permission.source.Source;
import com.yanzhenjie.permission.util.MainExecutor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by YanZhenjie on 2016/9/9.
 */
class MRequest implements PermissionRequest, RequestExecutor, PermissionActivity.RequestListener {

    private static final MainExecutor EXECUTOR = new MainExecutor();
    private static final PermissionChecker STANDARD_CHECKER = new StandardChecker();
    private static final PermissionChecker DOUBLE_CHECKER = new DoubleChecker();

    private Source mSource;

    private String[] mPermissions;
    private Rationale<List<String>> mRationale = new Rationale<List<String>>() {
        @Override
        public void showRationale(Context context, List<String> data, RequestExecutor executor) {
            executor.execute();
        }
    };
    private Action<List<String>> mGranted;
    private Action<List<String>> mDenied;

    private String[] mDeniedPermissions;

    MRequest(Source source) {
        this.mSource = source;
    }

    @Override
    public PermissionRequest permission(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    @Override
    public PermissionRequest rationale(Rationale<List<String>> rationale) {
        this.mRationale = rationale;
        return this;
    }

    @Override
    public PermissionRequest onGranted(Action<List<String>> granted) {
        this.mGranted = granted;
        return this;
    }

    @Override
    public PermissionRequest onDenied(Action<List<String>> denied) {
        this.mDenied = denied;
        return this;
    }

    @Override
    public void start() {
        List<String> deniedList = getDeniedPermissions(STANDARD_CHECKER, mSource, mPermissions);
        mDeniedPermissions = deniedList.toArray(new String[deniedList.size()]);
        if (mDeniedPermissions.length > 0) {
            List<String> rationaleList = getRationalePermissions(mSource, mDeniedPermissions);
            if (rationaleList.size() > 0) {
                mRationale.showRationale(mSource.getContext(), rationaleList, this);
            } else {
                execute();
            }
        } else {
            dispatchCallback();
        }
    }

    @Override
    public void execute() {
        PermissionActivity.requestPermission(mSource.getContext(), mDeniedPermissions, this);
    }

    @Override
    public void cancel() {
        dispatchCallback();
    }

    @Override
    public void onRequestCallback() {
        EXECUTOR.postDelayed(new Runnable() {
            @Override
            public void run() {
                dispatchCallback();
            }
        }, 100);
    }

    private void dispatchCallback() {
        List<String> deniedList = getDeniedPermissions(DOUBLE_CHECKER, mSource, mPermissions);
        if (deniedList.isEmpty()) {
            callbackSucceed();
        } else {
            callbackFailed(deniedList);
        }
    }

    /**
     * Callback acceptance status.
     */
    private void callbackSucceed() {
        if (mGranted != null) {
            List<String> permissionList = asList(mPermissions);
            try {
                mGranted.onAction(permissionList);
            } catch (Exception e) {
                Log.e("AndPermission", "Please check the onGranted() method body for bugs.", e);
                if (mDenied != null) {
                    mDenied.onAction(permissionList);
                }
            }
        }
    }

    /**
     * Callback rejected state.
     */
    private void callbackFailed(List<String> deniedList) {
        if (mDenied != null) {
            mDenied.onAction(deniedList);
        }
    }

    /**
     * Get denied permissions.
     */
    private static List<String> getDeniedPermissions(PermissionChecker checker, Source source, String... permissions) {
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (!checker.hasPermission(source.getContext(), permission)) {
                deniedList.add(permission);
            }
        }
        return deniedList;
    }

    /**
     * Get permissions to show rationale.
     */
    private static List<String> getRationalePermissions(Source source, String... permissions) {
        List<String> rationaleList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (source.isShowRationalePermission(permission)) {
                rationaleList.add(permission);
            }
        }
        return rationaleList;
    }
}