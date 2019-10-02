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
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;

import com.yanzhenjie.permission.util.StringUtils;

import java.util.TimeZone;

/**
 * Created by Zhenjie Yan on 2018/1/15.
 */
class CalendarWriteTest implements PermissionTest {

    private static final String NAME = StringUtils.hexToText("5045524D495353494F4E");
    private static final String ACCOUNT = StringUtils.hexToText("7065726D697373696F6E40676D61696C2E636F6D");

    private ContentResolver mResolver;

    CalendarWriteTest(Context context) {
        this.mResolver = context.getContentResolver();
    }

    @Override
    public boolean test() throws Throwable {
        try {
            TimeZone timeZone = TimeZone.getDefault();
            ContentValues value = new ContentValues();
            value.put(CalendarContract.Calendars.NAME, NAME);
            value.put(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT);
            value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
            value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, NAME);
            value.put(CalendarContract.Calendars.VISIBLE, 1);
            value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
            value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
            value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
            value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
            value.put(CalendarContract.Calendars.OWNER_ACCOUNT, NAME);
            value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

            Uri insertUri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
            Uri resourceUri = mResolver.insert(insertUri, value);
            return ContentUris.parseId(resourceUri) > 0;
        } finally {
            Uri deleteUri = CalendarContract.Calendars.CONTENT_URI.buildUpon().build();
            mResolver.delete(deleteUri, CalendarContract.Calendars.ACCOUNT_NAME + "=?", new String[]{ACCOUNT});
        }
    }
}
