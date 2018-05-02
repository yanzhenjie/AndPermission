/*
 * Copyright © Yan Zhenjie
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

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.Rationale;

import java.util.List;

/**
 * <p>Permission request.</p>
 * Created by Yan Zhenjie on 2016/9/9.
 */
public interface PermissionRequest {

    /**
     * One or more permissions.
     */
    PermissionRequest permission(String... permissions);

    /**
     * Set request rationale.
     */
    PermissionRequest rationale(Rationale<List<String>> rationale);

    /**
     * Action to be taken when all permissions are granted.
     */
    PermissionRequest onGranted(Action<List<String>> granted);

    /**
     * Action to be taken when all permissions are denied.
     */
    PermissionRequest onDenied(Action<List<String>> denied);

    /**
     * Request permission.
     */
    void start();
}