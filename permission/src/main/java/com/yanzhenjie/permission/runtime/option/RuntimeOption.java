/*
 * Copyright 2019 Zhenjie Yan
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
package com.yanzhenjie.permission.runtime.option;

import androidx.annotation.NonNull;

import com.yanzhenjie.permission.runtime.Permission;
import com.yanzhenjie.permission.runtime.PermissionDef;
import com.yanzhenjie.permission.runtime.PermissionRequest;
import com.yanzhenjie.permission.runtime.setting.SettingRequest;

/**
 * Created by Zhenjie Yan on 2/22/19.
 */
public interface RuntimeOption {

    /**
     * One or more permissions.
     */
    PermissionRequest permission(@NonNull @PermissionDef String... permissions);

    /**
     * One or more permission groups.
     *
     * @param groups use constants in {@link Permission.Group}.
     */
    PermissionRequest permission(@NonNull String[]... groups);

    /**
     * Permission settings.
     */
    SettingRequest setting();
}