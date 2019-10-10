/*
 * Copyright 2019 Zhenjie Yan
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
import android.os.Build;

import androidx.annotation.NonNull;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.checker.PermissionChecker;
import com.yanzhenjie.permission.source.Source;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created Zhenjie Yan on 2019-10-10.
 */
abstract class BaseRequest implements PermissionRequest {

    private Source mSource;

    private Rationale<List<String>> mRationale = new Rationale<List<String>>() {
        @Override
        public void showRationale(Context context, List<String> data, RequestExecutor executor) {
            executor.execute();
        }
    };
    private Action<List<String>> mGranted;
    private Action<List<String>> mDenied;

    BaseRequest(Source source) {
        this.mSource = source;
    }

    @Override
    public PermissionRequest rationale(@NonNull Rationale<List<String>> rationale) {
        this.mRationale = rationale;
        return this;
    }

    @Override
    public PermissionRequest onGranted(@NonNull Action<List<String>> granted) {
        this.mGranted = granted;
        return this;
    }

    @Override
    public PermissionRequest onDenied(@NonNull Action<List<String>> denied) {
        this.mDenied = denied;
        return this;
    }

    /**
     * Why permissions are required.
     */
    final void showRationale(List<String> rationaleList, RequestExecutor executor) {
        mRationale.showRationale(mSource.getContext(), rationaleList, executor);
    }

    /**
     * Callback acceptance status.
     */
    final void callbackSucceed(List<String> grantedList) {
        if (mGranted != null) {
            mGranted.onAction(grantedList);
        }
    }

    /**
     * Callback rejected state.
     */
    final void callbackFailed(List<String> deniedList) {
        if (mDenied != null) {
            mDenied.onAction(deniedList);
        }
    }

    /**
     * Filter the permissions you want to apply; remove unsupported and duplicate permissions.
     */
    public static List<String> filterPermissions(List<String> permissions) {
        permissions = new ArrayList<>(new HashSet<>(permissions));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            permissions.remove(Permission.READ_PHONE_NUMBERS);
            permissions.remove(Permission.ANSWER_PHONE_CALLS);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissions.remove(Permission.ACTIVITY_RECOGNITION);
            permissions.remove(Permission.ACCESS_BACKGROUND_LOCATION);
        }
        return permissions;
    }

    /**
     * Get denied permissions.
     */
    public static List<String> getDeniedPermissions(PermissionChecker checker, Source source, List<String> permissions) {
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
    public static List<String> getRationalePermissions(Source source, List<String> deniedPermissions) {
        List<String> rationaleList = new ArrayList<>(1);
        for (String permission : deniedPermissions) {
            if (source.isShowRationalePermission(permission)) {
                rationaleList.add(permission);
            }
        }
        return rationaleList;
    }
}
