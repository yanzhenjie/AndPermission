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
package com.yanzhenjie.permission;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by YanZhenjie on 2018/4/27.
 */
public class CompatFileProvider extends FileProvider {

    /**
     * Get the provider's authority.
     *
     * @param context context.
     * @return {@code ${applicationId}.apk.install}.
     */
    public static String getAuthority(Context context) {
        return context.getPackageName() + ".file.path.share";
    }

    /**
     * Get compatible Android 7.0 and lower versions of Uri.
     *
     * @param context context.
     * @param file    file.
     * @return uri.
     */
    public static Uri getFileUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getUriForFile(context, CompatFileProvider.getAuthority(context), file);
        }
        return Uri.fromFile(file);
    }
}