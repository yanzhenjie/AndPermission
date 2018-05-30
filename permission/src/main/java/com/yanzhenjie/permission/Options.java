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
package com.yanzhenjie.permission;

import android.os.Build;

import com.yanzhenjie.permission.install.InstallRequest;
import com.yanzhenjie.permission.install.NRequestFactory;
import com.yanzhenjie.permission.install.ORequestFactory;
import com.yanzhenjie.permission.overlay.LRequestFactory;
import com.yanzhenjie.permission.overlay.MRequestFactory;
import com.yanzhenjie.permission.overlay.OverlayRequest;
import com.yanzhenjie.permission.runtime.PermissionRequest;
import com.yanzhenjie.permission.runtime.Runtime;
import com.yanzhenjie.permission.source.Source;

/**
 * Created by YanZhenjie on 2018/4/28.
 */
public class Options {

    private static final InstallRequestFactory INSTALL_REQUEST_FACTORY;
    private static final OverlayRequestFactory OVERLAY_REQUEST_FACTORY;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            INSTALL_REQUEST_FACTORY = new ORequestFactory();
        } else {
            INSTALL_REQUEST_FACTORY = new NRequestFactory();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            OVERLAY_REQUEST_FACTORY = new MRequestFactory();
        } else {
            OVERLAY_REQUEST_FACTORY = new LRequestFactory();
        }
    }

    public interface InstallRequestFactory {
        /**
         * Create apk installer request.
         */
        InstallRequest create(Source source);
    }

    public interface OverlayRequestFactory {
        /**
         * Create overlay request.
         */
        OverlayRequest create(Source source);
    }

    private Source mSource;

    Options(Source source) {
        this.mSource = source;
    }

    /**
     * One or more permissions.
     *
     * @deprecated use {@link Options#runtime()} instead.
     */
    @Deprecated
    public PermissionRequest permission(String... permissions) {
        return runtime().permission(permissions);
    }

    /**
     * One or more permission groups.
     *
     * @deprecated use {@link Options#runtime()} instead.
     */
    @Deprecated
    public PermissionRequest permission(String[]... groups) {
        return runtime().permission(groups);
    }

    /**
     * Handle runtime permissions.
     */
    public Runtime runtime() {
        return new Runtime(mSource);
    }

    /**
     * Handle request package install permission.
     */
    public InstallRequest install() {
        return INSTALL_REQUEST_FACTORY.create(mSource);
    }

    /**
     * Handle overlay permission.
     */
    public OverlayRequest overlay() {
        return OVERLAY_REQUEST_FACTORY.create(mSource);
    }
}