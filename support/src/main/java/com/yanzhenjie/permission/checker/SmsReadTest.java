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

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;

/**
 * Created by Zhenjie Yan on 2018/1/25.
 */
class SmsReadTest implements PermissionTest {

    private ContentResolver mResolver;

    SmsReadTest(Context context) {
        mResolver = context.getContentResolver();
    }

    @Override
    public boolean test() throws Throwable {
        String[] projection = new String[] {Telephony.Sms._ID, Telephony.Sms.ADDRESS, Telephony.Sms.PERSON,
            Telephony.Sms.BODY};
        Cursor cursor = mResolver.query(Telephony.Sms.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            try {
                CursorTest.read(cursor);
            } finally {
                cursor.close();
            }
            return true;
        } else {
            return false;
        }
    }
}