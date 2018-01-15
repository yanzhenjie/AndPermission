package com.yanzhenjie.permission.checker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;

import java.util.TimeZone;

/**
 * Created by YanZhenjie on 2018/1/15.
 */
@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class CalendarWriteTest implements PermissionTest {

    private static final String NAME = "PERMISSION";
    private static final String ACCOUNT = "permission@gmail.com";

    private ContentResolver mResolver;

    CalendarWriteTest(ContentResolver resolver) {
        this.mResolver = resolver;
    }

    @Override
    public void test() throws Throwable {
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
            mResolver.insert(insertUri, value);
        } finally {
            Uri deleteUri = CalendarContract.Calendars.CONTENT_URI.buildUpon().build();
            mResolver.delete(deleteUri, CalendarContract.Calendars.ACCOUNT_NAME + "=?", new String[]{ACCOUNT});
        }
    }
}
