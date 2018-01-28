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

import android.support.annotation.NonNull;

import com.yanzhenjie.permission.checker.PermissionChecker;
import com.yanzhenjie.permission.checker.StrictChecker;
import com.yanzhenjie.permission.source.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by YanZhenjie on 2018/1/25.
 */
class LRequest implements Request {

    private static final PermissionChecker CHECKER = new StrictChecker();

    private Source mSource;

    private String[] mPermissions;
    private Action mGranted;
    private Action mDenied;

    LRequest(Source source) {
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
    public Request rationale(Rationale listener) {
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
        List<String> deniedList = getDeniedPermissions(mSource, mPermissions);
        if (deniedList.isEmpty())
            callbackSucceed();
        else
            callbackFailed(deniedList);
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
                if (mDenied != null) {
                    mDenied.onAction(permissionList);
                }
            }
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
            if (!CHECKER.hasPermission(source.getContext(), permission)) {
                deniedList.add(permission);
            }
        }
        return deniedList;
    }
}