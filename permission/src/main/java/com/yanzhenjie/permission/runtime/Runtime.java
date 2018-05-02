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
package com.yanzhenjie.permission.runtime;

import android.os.Build;

import com.yanzhenjie.permission.Setting;
import com.yanzhenjie.permission.runtime.setting.RuntimeSetting;
import com.yanzhenjie.permission.source.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by YanZhenjie on 2018/5/2.
 */
public class Runtime {

    private static final PermissionRequestFactory FACTORY;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FACTORY = new MRequestFactory();
        } else {
            FACTORY = new LRequestFactory();
        }
    }

    public interface PermissionRequestFactory {
        /**
         * Create permission request.
         */
        PermissionRequest create(Source source);
    }

    private Source mSource;

    public Runtime(Source source) {
        this.mSource = source;
    }

    /**
     * One or more permissions.
     */
    public PermissionRequest permission(String... permissions) {
        return FACTORY.create(mSource).permission(permissions);
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
        return FACTORY.create(mSource).permission(permissions);
    }

    /**
     * Permission settings.
     */
    public Setting setting() {
        return new RuntimeSetting(mSource);
    }
}