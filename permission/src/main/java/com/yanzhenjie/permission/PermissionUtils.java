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
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

/**
 * Created by Yan Zhenjie on 2016/9/10.
 */
class PermissionUtils {

    static Context getContext(Object o) {
        if (o instanceof Activity)
            return (Activity) o;
        else if (o instanceof Fragment)
            return ((Fragment) o).getActivity();
        else if (o instanceof android.app.Fragment)
            return ((android.app.Fragment) o).getActivity();
        throw new IllegalArgumentException("The " + o.getClass().getName() + " is not support.");
    }

    static boolean shouldShowRationalePermissions(Object o, String... permissions) {
        if (Build.VERSION.SDK_INT < 23) return false;
        boolean rationale = false;
        for (String permission : permissions) {
            if (o instanceof Activity) {
                rationale = ActivityCompat.shouldShowRequestPermissionRationale((Activity) o, permission);
            } else if (o instanceof android.support.v4.app.Fragment) {
                rationale = ((android.support.v4.app.Fragment) o).shouldShowRequestPermissionRationale(permission);
            } else if (o instanceof android.app.Fragment) {
                rationale = ((android.app.Fragment) o).shouldShowRequestPermissionRationale(permission);
            }
            if (rationale) return true;
        }
        return false;
    }

}
