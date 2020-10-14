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
package com.yanzhenjie.permission.task;

import android.os.AsyncTask;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Zhenjie Yan on 2019-09-23.
 */
public abstract class TaskExecutor<T> extends AsyncTask<Void, Void, T> {

    private static final Executor sExecutor = Executors.newSingleThreadExecutor();

    public TaskExecutor() {
    
    }

    @Override
    protected final void onPreExecute() {

    }

    @Override
    protected final void onPostExecute(T t) {
        onFinish(t);
    }

    protected abstract void onFinish(T t);

    /**
     * Just call this method.
     */
    public final void execute() {
        executeOnExecutor(sExecutor);
    }
}