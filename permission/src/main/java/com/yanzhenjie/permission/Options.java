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
import com.yanzhenjie.permission.runtimes.LRequestFactory;
import com.yanzhenjie.permission.runtimes.MRequestFactory;
import com.yanzhenjie.permission.runtimes.PermissionRequest;
import com.yanzhenjie.permission.source.Source;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by YanZhenjie on 2018/4/28.
 */
public class Options {

    private static final PermissionRequestFactory PERMISSION_FACTORY;
    private static final InstallRequestFactory INSTALL_FACTORY;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PERMISSION_FACTORY = new MRequestFactory();
        } else {
            PERMISSION_FACTORY = new LRequestFactory();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            INSTALL_FACTORY = new ORequestFactory();
        } else {
            INSTALL_FACTORY = new NRequestFactory();
        }
    }

    public interface PermissionRequestFactory {
        /**
         * Create permission request.
         */
        PermissionRequest create(Source source);
    }

    public interface InstallRequestFactory {
        /**
         * Create apk installer request.
         */
        InstallRequest create(Source source);
    }

    private Source mSource;

    Options(Source source) {
        this.mSource = source;
    }

    /**
     * One or more permissions.
     */
    public PermissionRequest permission(String... permissions) {
        return PERMISSION_FACTORY.create(mSource).permission(permissions);
    }

    /**
     * One or more permission groups.
     */
    public PermissionRequest permission(String[]... groups) {
        List<String> permissionList = new ArrayList<>();
        for (String[] group : groups) {
            permissionList.addAll(Arrays.asList(group));
        }
        String[] permissions = permissionList.toArray(new String[permissionList.size()]);
        return PERMISSION_FACTORY.create(mSource).permission(permissions);
    }

    /**
     * The file path.
     */
    public InstallRequest install(String path) {
        return install(new File(path));
    }

    /**
     * The file.
     */
    public InstallRequest install(File file) {
        return INSTALL_FACTORY.create(mSource).file(file);
    }
}