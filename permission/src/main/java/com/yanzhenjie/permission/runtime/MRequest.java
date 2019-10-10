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
package com.yanzhenjie.permission.runtime;

import androidx.annotation.NonNull;

import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.bridge.BridgeRequest;
import com.yanzhenjie.permission.bridge.RequestManager;
import com.yanzhenjie.permission.checker.DoubleChecker;
import com.yanzhenjie.permission.checker.PermissionChecker;
import com.yanzhenjie.permission.checker.StandardChecker;
import com.yanzhenjie.permission.source.Source;
import com.yanzhenjie.permission.task.TaskExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Zhenjie Yan on 2016/9/9.
 */
class MRequest extends BaseRequest implements RequestExecutor, BridgeRequest.Callback {

    private static final PermissionChecker STANDARD_CHECKER = new StandardChecker();
    private static final PermissionChecker DOUBLE_CHECKER = new DoubleChecker();

    private Source mSource;

    private List<String> mPermissions;

    private List<String> mDeniedPermissions;

    MRequest(Source source) {
        super(source);
        this.mSource = source;
    }

    @Override
    public PermissionRequest permission(@NonNull String... permissions) {
        mPermissions = new ArrayList<>();
        mPermissions.addAll(Arrays.asList(permissions));
        return this;
    }

    @Override
    public PermissionRequest permission(@NonNull String[]... groups) {
        mPermissions = new ArrayList<>();
        for (String[] group : groups) {
            mPermissions.addAll(Arrays.asList(group));
        }
        return this;
    }

    @Override
    public void start() {
        mPermissions = filterPermissions(mPermissions);

        mDeniedPermissions = getDeniedPermissions(STANDARD_CHECKER, mSource, mPermissions);
        if (mDeniedPermissions.size() > 0) {
            List<String> rationaleList = getRationalePermissions(mSource, mDeniedPermissions);
            if (rationaleList.size() > 0) {
                showRationale(rationaleList, this);
            } else {
                execute();
            }
        } else {
            onCallback();
        }
    }

    @Override
    public void execute() {
        BridgeRequest request = new BridgeRequest(mSource);
        request.setType(BridgeRequest.TYPE_PERMISSION);
        request.setPermissions(mDeniedPermissions);
        request.setCallback(this);
        RequestManager.get().add(request);
    }

    @Override
    public void cancel() {
        onCallback();
    }

    @Override
    public void onCallback() {
        new TaskExecutor<List<String>>(mSource.getContext()) {
            @Override
            protected List<String> doInBackground(Void... voids) {
                return getDeniedPermissions(DOUBLE_CHECKER, mSource, mPermissions);
            }

            @Override
            protected void onFinish(List<String> deniedList) {
                if (deniedList.isEmpty()) {
                    callbackSucceed(mPermissions);
                } else {
                    callbackFailed(deniedList);
                }
            }
        }.execute();
    }
}