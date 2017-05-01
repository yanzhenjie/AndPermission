/*
 * Copyright © Yan Zhenjie. All Rights Reserved
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
public interface Request<T extends Request> {

    /**
     * Here to fill in all of this to apply for permission, can be a, can be more.
     *
     * @param permissions one or more permissions.
     * @return {@link Request}.
     */
    @NonNull
    T permission(String... permissions);

    /**
     * Request code.
     *
     * @param requestCode int, the first parameter in callback {@code onRequestPermissionsResult(int, String[],
     *                    int[])}}.
     * @return {@link Request}.
     */
    @NonNull
    T requestCode(int requestCode);

    /**
     * Set the callback object.
     *
     * @return {@link Request}.
     */
    T callback(Object callback);

    /**
     * Request permission.
     *
     * @deprecated use {@link #start()} instead.
     */
    @Deprecated
    void send();

    /**
     * Request permission.
     */
    void start();

}
