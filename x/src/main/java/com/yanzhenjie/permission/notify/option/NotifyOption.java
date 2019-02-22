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
package com.yanzhenjie.permission.notify.option;

import com.yanzhenjie.permission.notify.PermissionRequest;
import com.yanzhenjie.permission.notify.listener.ListenerRequest;

/**
 * Created by Zhenjie Yan on 2/14/19.
 */
public interface NotifyOption {

    /**
     * Handle permissions.
     */
    PermissionRequest permission();

    /**
     * Handle notify listener.
     */
    ListenerRequest listener();
}