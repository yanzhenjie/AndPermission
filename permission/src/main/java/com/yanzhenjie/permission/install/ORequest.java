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
package com.yanzhenjie.permission.install;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionActivity;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.source.Source;
import com.yanzhenjie.permission.util.MainExecutor;

import java.io.File;

/**
 * Created by YanZhenjie on 2018/4/28.
 */
class ORequest implements InstallRequest, RequestExecutor, PermissionActivity.RequestListener {

    private static final MainExecutor EXECUTOR = new MainExecutor();

    private Source mSource;

    private File mFile;
    private Rationale<File> mRationale = new Rationale<File>() {
        @Override
        public void showRationale(Context context, File data, RequestExecutor executor) {
            executor.execute();
        }
    };
    private Action<File> mGranted;
    private Action<File> mDenied;

    ORequest(Source source) {
        this.mSource = source;
    }

    @Override
    public InstallRequest file(File file) {
        this.mFile = file;
        return this;
    }

    @Override
    public InstallRequest rationale(Rationale<File> rationale) {
        this.mRationale = rationale;
        return this;
    }

    @Override
    public InstallRequest onGranted(Action<File> granted) {
        this.mGranted = granted;
        return this;
    }

    @Override
    public InstallRequest onDenied(Action<File> denied) {
        this.mDenied = denied;
        return this;
    }

    @Override
    public void start() {
        if (mSource.canRequestPackageInstalls()) {
            callbackSucceed();
            install();
        } else {
            mRationale.showRationale(mSource.getContext(), mFile, this);
        }
    }

    @Override
    public void execute() {
        PermissionActivity.requestInstall(mSource.getContext(), this);
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
                if (mSource.canRequestPackageInstalls()) {
                    callbackSucceed();
                    install();
                } else {
                    callbackFailed();
                }
            }
        }, 100);
    }

    /**
     * Start the installation.
     */
    private void install() {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = AndPermission.getFileUri(mSource.getContext(), mFile);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        mSource.startActivity(intent);
    }

    /**
     * Callback acceptance status.
     */
    private void callbackSucceed() {
        if (mGranted != null) {
            mGranted.onAction(mFile);
        }
    }

    /**
     * Callback rejected state.
     */
    private void callbackFailed() {
        if (mDenied != null) {
            mDenied.onAction(mFile);
        }
    }
}