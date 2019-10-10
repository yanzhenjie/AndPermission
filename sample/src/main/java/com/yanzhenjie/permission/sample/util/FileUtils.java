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
package com.yanzhenjie.permission.sample.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.io.File;

/**
 * Created Zhenjie Yan on 2019-10-10.
 */
public class FileUtils {

    public static File getFileDir(Context context) {
        return getFileDir(context, null);
    }

    public static File getFileDir(Context context, @Nullable String type) {
        File root = context.getFilesDir();
        if (TextUtils.isEmpty(type)) {
            return root;
        } else {
            File dir = new File(root, type);
            createDir(dir);
            return dir;
        }
    }

    public static boolean externalAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static File getExternalDir(Context context) {
        return getExternalDir(context, null);
    }

    public static File getExternalDir(Context context, @Nullable String type) {
        if (externalAvailable()) {
            if (TextUtils.isEmpty(type)) {
                return context.getExternalFilesDir(null);
            }

            File dir = context.getExternalFilesDir(type);
            if (dir == null) {
                dir = context.getExternalFilesDir(null);
                dir = new File(dir, type);
                createDir(dir);
            }

            return dir;
        }
        throw new RuntimeException("External storage device is not available.");
    }

    public static File getRootDir() {
        return getRootDir(null);
    }

    public static File getRootDir(@Nullable String type) {
        if (externalAvailable()) {
            File root = Environment.getExternalStorageDirectory();
            File appRoot = new File(root, "AndPermission");
            createDir(appRoot);

            if (TextUtils.isEmpty(type)) {
                return appRoot;
            }

            File dir = new File(appRoot, type);
            createDir(dir);

            return dir;
        }
        throw new RuntimeException("External storage device is not available.");
    }

    public static void createDir(File dir) {
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                dir.delete();
            }
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
