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

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.checker.PermissionChecker;
import com.yanzhenjie.permission.checker.StrictChecker;
import com.yanzhenjie.permission.source.Source;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by YanZhenjie on 2018/1/25.
 */
class LRequest implements PermissionRequest {

    private static final PermissionChecker CHECKER = new StrictChecker();

    private Source mSource;

    private String[] mPermissions;
    private Action<List<String>> mGranted;
    private Action<List<String>> mDenied;

    LRequest(Source source) {
        this.mSource = source;
    }

    @Override
    public PermissionRequest permission(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    @Override
    public PermissionRequest rationale(Rationale<List<String>> rationale) {
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
    private void callbackFailed(List<String> deniedList) {
        if (mDenied != null) {
            mDenied.onAction(deniedList);
        }
    }

    /**
     * Get denied permissions.
     */
    private static List<String> getDeniedPermissions(Source source, String... permissions) {
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (!CHECKER.hasPermission(source.getContext(), permission)) {
                deniedList.add(permission);
            }
        }
        return deniedList;
    }
}