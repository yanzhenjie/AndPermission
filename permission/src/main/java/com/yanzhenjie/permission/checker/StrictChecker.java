/*
 *
 *  * Copyright © Yan Zhenjie
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package com.yanzhenjie.permission.checker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.yanzhenjie.permission.ApLog;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.List;

/**
 * Created by YanZhenjie on 2018/1/7.
 */
public class StrictChecker implements PermissionChecker {

    private PermissionChecker mChecker = new StandardChecker();

    public StrictChecker() {
    }

    @Override
    public boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return true;

        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return true;

        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean hasPermission(Context context, String permission) {
        try {
            switch (permission) {
                case Permission.READ_CALENDAR:
                    return checkReadCalendar(context);
                case Permission.WRITE_CALENDAR:
                    return checkWriteCalendar(context);
                case Permission.CAMERA:
                    return checkCamera(context);
                case Permission.READ_CONTACTS:
                    return checkReadContacts(context);
                case Permission.WRITE_CONTACTS:
                    return checkWriteContacts(context);
                case Permission.GET_ACCOUNTS:
                    return mChecker.hasPermission(context, permission);
                case Permission.ACCESS_COARSE_LOCATION:
                case Permission.ACCESS_FINE_LOCATION:
                    return checkLocation(context);
                case Permission.RECORD_AUDIO:
                    return checkRecordAudio();
                case Permission.READ_PHONE_STATE:
                    return checkReadPhoneState(context);
                case Permission.CALL_PHONE:
                    return mChecker.hasPermission(context, permission);
                case Permission.READ_CALL_LOG:
                    return checkReadCallLog(context);
                case Permission.WRITE_CALL_LOG:
                    return checkWriteCallLog(context);
                case Permission.ADD_VOICEMAIL:
                    return mChecker.hasPermission(context, permission);
                case Permission.USE_SIP:
                    return mChecker.hasPermission(context, permission);
                case Permission.PROCESS_OUTGOING_CALLS:
                    return mChecker.hasPermission(context, permission);
                case Permission.BODY_SENSORS:
                    return mChecker.hasPermission(context, permission);
                case Permission.SEND_SMS:
                case Permission.RECEIVE_MMS:
                    return mChecker.hasPermission(context, permission);
                case Permission.READ_SMS:
                    return checkReadSms(context);
                case Permission.RECEIVE_WAP_PUSH:
                case Permission.RECEIVE_SMS:
                    return mChecker.hasPermission(context, permission);
                case Permission.READ_EXTERNAL_STORAGE:
                    return checkReadStorage();
                case Permission.WRITE_EXTERNAL_STORAGE:
                    return checkWriteStorage();
                default:
                    return mChecker.hasPermission(context, permission);
            }
        } catch (Throwable e) {
            ApLog.e(e);
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static boolean checkReadCalendar(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(CalendarContract.Calendars.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static boolean checkWriteCalendar(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        PermissionTest test = new CalendarWriteTest(resolver);
        return test.test();
    }

    private static boolean checkCamera(Context context) throws Throwable {
        PermissionTest test = new CameraTest(context);
        return test.test();
    }

    private static boolean checkReadContacts(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkWriteContacts(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        PermissionTest test = new ContactsWriteTest(resolver);
        return test.test();
    }

    private static boolean checkLocation(Context context) throws Throwable {
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            return true;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            PermissionTest test = new LocationTest(locationManager);
            return test.test();
        }
    }

    private static boolean checkRecordAudio() throws Throwable {
        PermissionTest test = new RecordAudioTest();
        return test.test();
    }

    private static boolean checkReadPhoneState(Context context) throws Throwable {
        TelephonyManager service = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return !TextUtils.isEmpty(service.getDeviceId()) || !TextUtils.isEmpty(service.getSubscriberId());
    }

    private static boolean checkReadCallLog(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkWriteCallLog(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        PermissionTest test = new CallLogWriteTest(resolver);
        return test.test();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static boolean checkReadSms(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkReadStorage() throws Throwable {
        File directory = Environment.getExternalStorageDirectory();
        return directory.canRead() && directory.list() != null;
    }

    private static boolean checkWriteStorage() throws Throwable {
        File directory = Environment.getExternalStorageDirectory();
        File tempFile = File.createTempFile("permission", "test", directory);
        return directory.canWrite() && tempFile.exists() && tempFile.delete();
    }
}