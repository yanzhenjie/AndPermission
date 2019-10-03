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
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Created by Zhenjie Yan on 2018/1/25.
 */
class PhoneStateReadTest implements PermissionTest {

    private Context mContext;

    PhoneStateReadTest(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean test() throws Throwable {
        PackageManager packageManager = mContext.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) return true;

        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) telephonyManager.getDeviceId();
        else telephonyManager.getDeviceSoftwareVersion();
        return true;
    }
}