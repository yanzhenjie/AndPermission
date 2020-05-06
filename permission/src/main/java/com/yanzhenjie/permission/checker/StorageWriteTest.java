/*
 * Copyright © Zhenjie Yan
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
package com.yanzhenjie.permission.checker;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by Zhenjie Yan on 2018/1/16.
 */
class StorageWriteTest implements PermissionTest {

    private Context mContext;

    StorageWriteTest(Context c) {
        mContext = c;
    }

    @Override
    public boolean test() throws Throwable {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Environment.isExternalStorageLegacy()) return true;

        if (!TextUtils.equals(Environment.MEDIA_MOUNTED, Environment.getExternalStorageState())) return true;

        File directory = mContext.getExternalFilesDir(null);

        if (!directory.exists()) return true;

        File parent = new File(directory, "Android");
        if (parent.exists() && parent.isFile()) {
            if (!parent.delete()) return false;
        }
        if (!parent.exists()) {
            if (!parent.mkdirs()) return false;
        }
        File file = new File(parent, "ANDROID.PERMISSION.TEST");
        if (file.exists()) {
            return file.delete();
        } else {
            return file.createNewFile();
        }
    }
}