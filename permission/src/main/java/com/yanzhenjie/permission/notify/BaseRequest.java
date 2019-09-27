/*
 * Copyright 2018 Zhenjie Yan
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
package com.yanzhenjie.permission.notify;

import android.content.Context;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.source.Source;

/**
 * Created by Zhenjie Yan on 2018/6/1.
 */
abstract class BaseRequest implements PermissionRequest {

    private Source mSource;

    private Rationale<Void> mRationale = new Rationale<Void>() {
        @Override
        public void showRationale(Context context, Void data, RequestExecutor executor) {
            executor.execute();
        }
    };
    private Action<Void> mGranted;
    private Action<Void> mDenied;

    BaseRequest(Source source) {
        this.mSource = source;
    }

    @Override
    public final PermissionRequest rationale(Rationale<Void> rationale) {
        this.mRationale = rationale;
        return this;
    }

    @Override
    public final PermissionRequest onGranted(Action<Void> granted) {
        this.mGranted = granted;
        return this;
    }

    @Override
    public final PermissionRequest onDenied(Action<Void> denied) {
        this.mDenied = denied;
        return this;
    }

    /**
     * Why permissions are required.
     */
    final void showRationale(RequestExecutor executor) {
        mRationale.showRationale(mSource.getContext(), null, executor);
    }

    /**
     * Callback acceptance status.
     */
    final void callbackSucceed() {
        if (mGranted != null) {
            mGranted.onAction(null);
        }
    }

    /**
     * Callback rejected state.
     */
    final void callbackFailed() {
        if (mDenied != null) {
            mDenied.onAction(null);
        }
    }
}