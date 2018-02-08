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
package com.yanzhenjie.permission;

import android.support.annotation.NonNull;

/**
 * <p>Permission request.</p>
 * Created by Yan Zhenjie on 2016/9/9.
 */
public interface Request {

    /**
     * One or more permissions.
     */
    @NonNull
    Request permission(String... permissions);

    /**
     * One or more permission groups.
     */
    @NonNull
    Request permission(String[]... groups);

    /**
     * Set request rationale.
     */
    @NonNull
    Request rationale(Rationale listener);

    /**
     * Action to be taken when all permissions are granted.
     */
    @NonNull
    Request onGranted(Action granted);

    /**
     * Action to be taken when all permissions are denied.
     */
    @NonNull
    Request onDenied(Action denied);

    /**
     * Request permission.
     */
    void start();

}