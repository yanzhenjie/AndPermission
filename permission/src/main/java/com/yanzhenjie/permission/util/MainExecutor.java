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
package com.yanzhenjie.permission.util;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by YanZhenjie on 2018/4/28.
 */
public class MainExecutor {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    public MainExecutor() {
    }

    public void post(Runnable r) {
        HANDLER.post(r);
    }

    public void postDelayed(Runnable r, long delayMillis) {
        HANDLER.postDelayed(r, delayMillis);
    }
}