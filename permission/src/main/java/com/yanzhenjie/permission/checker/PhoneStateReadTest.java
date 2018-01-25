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

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by YanZhenjie on 2018/1/25.
 */
class PhoneStateReadTest implements PermissionTest {

    private Context mContext;

    PhoneStateReadTest(Context context) {
        this.mContext = context;
    }

    @SuppressLint("HardwareIds")
    @Override
    public boolean test() throws Throwable {
        TelephonyManager service = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return !TextUtils.isEmpty(service.getDeviceId()) || !TextUtils.isEmpty(service.getSubscriberId());
    }
}