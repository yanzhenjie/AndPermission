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
package com.yanzhenjie.permission.sample.app;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by YanZhenjie on 2018/5/2.
 */
public class WriteApkTask extends AsyncTask<Void, Void, Boolean> {

    private AssetManager mAssetManager;
    private Runnable mRunnable;

    public WriteApkTask(Context context, Runnable runnable) {
        this.mAssetManager = context.getAssets();
        this.mRunnable = runnable;
    }

    @Override
    protected void onPostExecute(Boolean b) {
        if (b) mRunnable.run();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            InputStream input = mAssetManager.open("android.apk");
            File apk = new File(Environment.getExternalStorageDirectory(), "android.apk");
            if (apk.exists()) return true;
            OutputStream output = new FileOutputStream(apk);
            byte[] buffer = new byte[input.available()];
            int len = input.read(buffer);
            output.write(buffer, 0, len);
            output.flush();
            output.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}