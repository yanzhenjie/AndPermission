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

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.PermissionActivity;
import com.yanzhenjie.permission.R;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.source.Source;
import com.yanzhenjie.permission.util.MainExecutor;

/**
 * Created by YanZhenjie on 2018/5/29.
 */
class LRequest implements OverlayRequest, RequestExecutor, PermissionActivity.RequestListener {

    private static final MainExecutor EXECUTOR = new MainExecutor();

    private Source mSource;

    private Rationale<Void> mRationale = new Rationale<Void>() {
        @Override
        public void showRationale(Context context, Void data, RequestExecutor executor) {
            executor.execute();
        }
    };
    private Action<Void> mGranted;
    private Action<Void> mDenied;

    LRequest(Source source) {
        mSource = source;
    }

    @Override
    public OverlayRequest rationale(Rationale<Void> rationale) {
        this.mRationale = rationale;
        return this;
    }

    @Override
    public OverlayRequest onGranted(Action<Void> granted) {
        this.mGranted = granted;
        return this;
    }

    @Override
    public OverlayRequest onDenied(Action<Void> denied) {
        this.mDenied = denied;
        return this;
    }

    @Override
    public void start() {
        if (hasPermission()) {
            callbackSucceed();
        } else {
            mRationale.showRationale(mSource.getContext(), null, this);
        }
    }

    @Override
    public void execute() {
        PermissionActivity.requestAlertWindow(mSource.getContext(), this);
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
                if (hasPermission()) {
                    callbackSucceed();
                } else {
                    callbackFailed();
                }
            }
        }, 100);
    }

    /**
     * Callback acceptance status.
     */
    private void callbackSucceed() {
        if (mGranted != null) {
            mGranted.onAction(null);
        }
    }

    /**
     * Callback rejected state.
     */
    private void callbackFailed() {
        if (mDenied != null) {
            mDenied.onAction(null);
        }
    }

    private boolean hasPermission() {
        Dialog dialog = new Dialog(mSource.getContext(), R.style.Permission_Theme_Dialog);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        try {
            dialog.show();
        } catch (Exception e) {
            return false;
        } finally {
            if (dialog.isShowing()) dialog.dismiss();
        }
        return true;
    }
}