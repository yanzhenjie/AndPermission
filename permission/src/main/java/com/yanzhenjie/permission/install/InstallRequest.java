/*
 * Copyright 2018 Zhenjie Yan
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

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.Rationale;

import java.io.File;

/**
 * Created by Zhenjie Yan on 2018/4/28.
 */
public interface InstallRequest {

    /**
     * The apk file.
     *
     * @param file apk file.
     */
    InstallRequest file(File file);

    /**
     * Set request rationale.
     */
    InstallRequest rationale(Rationale<File> rationale);

    /**
     * Action to be taken when all permissions are granted.
     */
    InstallRequest onGranted(Action<File> granted);

    /**
     * Action to be taken when all permissions are denied.
     */
    InstallRequest onDenied(Action<File> denied);

    /**
     * Start install.
     */
    void start();
}