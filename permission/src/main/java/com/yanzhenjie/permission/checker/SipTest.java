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
import android.net.sip.SipManager;
import android.net.sip.SipProfile;

import com.yanzhenjie.permission.util.StringUtils;

/**
 * Created by Zhenjie Yan on 2018/1/25.
 */
class SipTest implements PermissionTest {

    private static final String USER = StringUtils.hexToText("5065726D697373696F6E");
    private static final String IP = StringUtils.hexToText("3132372E302E302E31");
    private static final String PASSWORD = StringUtils.textToHex("70617373776F7264");

    private Context mContext;

    SipTest(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean test() throws Throwable {
        if (!SipManager.isApiSupported(mContext)) {
            return true;
        }
        SipManager manager = SipManager.newInstance(mContext);
        if (manager == null) {
            return true;
        }
        SipProfile.Builder builder = new SipProfile.Builder(USER, IP);
        builder.setPassword(PASSWORD);
        SipProfile profile = builder.build();
        manager.open(profile);
        manager.close(profile.getUriString());
        return true;
    }
}