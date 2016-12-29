/*
 * Copyright 2016 Yan Zhenjie
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;

/**
 * Created by Yan Zhenjie on 2016/12/28.
 */
class SettingExecutor implements SettingService {

    private Object object;
    private int requestCode;

    SettingExecutor(@NonNull Object mObject, int mRequestCode) {
        object = mObject;
        requestCode = mRequestCode;
    }

    @Override
    public void execute() {
        Context context = PermissionUtils.getContext(object);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        startForResult(object, intent, requestCode);
    }

    @Override
    public void cancel() {
    }

    private static void startForResult(Object object, Intent intent, int requestCode) {
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, requestCode);
        } else if (object instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) object).startActivityForResult(intent, requestCode);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).startActivityForResult(intent, requestCode);
        }
    }

}