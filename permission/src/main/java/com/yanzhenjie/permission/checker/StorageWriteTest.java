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
package com.yanzhenjie.permission.checker;

import android.os.Environment;

import java.io.File;

/**
 * Created by YanZhenjie on 2018/1/16.
 */
class StorageWriteTest implements PermissionTest {

    StorageWriteTest() {
    }

    @Override
    public boolean test() throws Throwable {
        File directory = Environment.getExternalStorageDirectory();
        if (!directory.exists() || !directory.canWrite()) return false;
        File parent = new File(directory, "Android");
        if (parent.exists() && parent.isFile()) if (!parent.delete()) return false;
        if (!parent.exists()) if (!parent.mkdirs()) return false;
        File file = new File(parent, "ANDROID.PERMISSION.TEST");
        if (file.exists()) return file.delete();
        else return file.createNewFile();
    }
}