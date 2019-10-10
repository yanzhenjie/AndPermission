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

import com.yanzhenjie.permission.checker.PermissionChecker;
import com.yanzhenjie.permission.checker.StrictChecker;
import com.yanzhenjie.permission.source.Source;
import com.yanzhenjie.permission.task.TaskExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Zhenjie Yan on 2018/1/25.
 */
class LRequest extends BaseRequest {

    private static final PermissionChecker STRICT_CHECKER = new StrictChecker();

    private Source mSource;

    private List<String> mPermissions;

    LRequest(Source source) {
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

        new TaskExecutor<List<String>>(mSource.getContext()) {
            @Override
            protected List<String> doInBackground(Void... voids) {
                return getDeniedPermissions(STRICT_CHECKER, mSource, mPermissions);
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