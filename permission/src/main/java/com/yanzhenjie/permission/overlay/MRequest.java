/*
 * Copyright 2018 Yan Zhenjie
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
package com.yanzhenjie.permission.overlay;

import com.yanzhenjie.permission.PermissionActivity;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.source.Source;
import com.yanzhenjie.permission.util.MainExecutor;

/**
 * Created by YanZhenjie on 2018/5/29.
 */
class MRequest extends BaseRequest implements RequestExecutor, PermissionActivity.RequestListener {

    private static final MainExecutor EXECUTOR = new MainExecutor();

    private Source mSource;


    MRequest(Source source) {
        super(source);
        this.mSource = source;
    }

    @Override
    public void start() {
        if (mSource.canDrawOverlays()) {
            dispatchCallback();
        } else {
            showRationale(this);
        }
    }

    @Override
    public void execute() {
        PermissionActivity.requestOverlay(mSource.getContext(), this);
    }

    @Override
    public void cancel() {
        callbackFailed();
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
        if (mSource.canDrawOverlays() && tryDisplayDialog(mSource.getContext())) {
            callbackSucceed();
        } else {
            callbackFailed();
        }
    }
}