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

import android.content.Intent;
import android.net.Uri;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionActivity;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.source.Source;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YanZhenjie on 2018/4/28.
 */
class ORequest implements InstallRequest, RequestExecutor, PermissionActivity.RequestListener {

    private Source mSource;

    private File mFile;
    private Rationale mRationaleListener = Rationale.DEFAULT;
    private Action mGranted;
    private Action mDenied;

    ORequest(Source source) {
        this.mSource = source;
    }

    @Override
    public InstallRequest file(File file) {
        this.mFile = file;
        return this;
    }

    @Override
    public InstallRequest rationale(Rationale listener) {
        this.mRationaleListener = listener;
        return this;
    }

    @Override
    public InstallRequest onGranted(Action granted) {
        this.mGranted = granted;
        return this;
    }

    @Override
    public InstallRequest onDenied(Action denied) {
        this.mDenied = denied;
        return this;
    }

    @Override
    public void start() {
        if (mSource.canRequestPackageInstalls()) {
            callbackSucceed();
            install();
        } else {
            List<String> permissions = new ArrayList<>();
            permissions.add(Permission.PackageInstall.REQUEST_INSTALL_PACKAGES);
            mRationaleListener.showRationale(mSource.getContext(), permissions, this);
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
        if (mSource.canRequestPackageInstalls()) {
            callbackSucceed();
            install();
        } else {
            callbackFailed();
        }
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
            List<String> permissions = new ArrayList<>();
            permissions.add(Permission.PackageInstall.REQUEST_INSTALL_PACKAGES);
            mGranted.onAction(permissions);
        }
    }

    /**
     * Callback rejected state.
     */
    private void callbackFailed() {
        if (mDenied != null) {
            List<String> permissions = new ArrayList<>();
            permissions.add(Permission.PackageInstall.REQUEST_INSTALL_PACKAGES);
            mDenied.onAction(permissions);
        }
    }
}