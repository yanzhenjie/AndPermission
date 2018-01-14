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
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by YanZhenjie on 2018/1/7.
 */
public class StrictChecker implements PermissionChecker {

    @Override
    public boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        return false;
    }

    @Override
    public boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions) {
        return false;
    }

    private static boolean isPermissionGranted(Context activity, String permission) {
        try {
            switch (permission) {
                case Permission.READ_CALENDAR:
                    return checkReadCalendar(activity);
                case Permission.WRITE_CALENDAR:
                    return true;
                case Permission.CAMERA:
                    return true;
                case Permission.READ_CONTACTS:
                    return checkReadContacts(activity);
                case Permission.WRITE_CONTACTS:
                    return checkWriteContacts(activity);
                case Permission.GET_ACCOUNTS:
                    return true;
                case Permission.ACCESS_COARSE_LOCATION:
                case Permission.ACCESS_FINE_LOCATION:
                    return checkLocation(activity);
                case Permission.RECORD_AUDIO:
                    return checkRecordAudio();
                case Permission.READ_PHONE_STATE:
                    return checkReadPhoneState(activity);
                case Permission.CALL_PHONE:
                    return true;
                case Permission.READ_CALL_LOG:
                    return checkReadCallLog(activity);
                case Permission.WRITE_CALL_LOG:
                    return checkWriteCallLog(activity);
                case Permission.ADD_VOICEMAIL:
                    return true;
                case Permission.USE_SIP:
                    return true;
                case Permission.PROCESS_OUTGOING_CALLS:
                    return true;
                case Permission.BODY_SENSORS:
                    return checkBodySensors(activity);
                case Permission.SEND_SMS:
                case Permission.RECEIVE_MMS:
                    return true;
                case Permission.READ_SMS:
                    return checkReadSms(activity);
                case Permission.RECEIVE_WAP_PUSH:
                case Permission.RECEIVE_SMS:
                    return true;
                case Permission.READ_EXTERNAL_STORAGE:
                    return checkReadStorage();
                case Permission.WRITE_EXTERNAL_STORAGE:
                    return checkWriteStorage();
                default:
                    return true;
            }
        } catch (Throwable e) {
            return false;
        }
    }

    private static boolean checkReadCalendar(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://com.android.calendar/calendars"), null, null, null, null);
        if (cursor != null) {
            cursor.close();
            return true;
        } else {
            return false;
        }
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
        test.test();
        return true;
    }

    private static boolean checkLocation(Context context) throws Throwable {
        final LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            return true;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            PermissionTest test = new LocationTest(locationManager);
            test.test();
            return true;
        }
    }

    private static boolean checkRecordAudio() throws Throwable {
        PermissionTest test = new RecordAudioTest();
        test.test();
        return true;
    }

    private static boolean checkReadPhoneState(Context context) throws Throwable {
        TelephonyManager service = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        return !TextUtils.isEmpty(service.getDeviceId()) || !TextUtils.isEmpty(service.getSubscriberId());
    }

    private static boolean checkReadCallLog(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://call_log/calls"), null, null, null, null);
        if (cursor != null) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkWriteCallLog(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        CallLogWriteTest test = new CallLogWriteTest(resolver);
        test.test();
        return true;
    }

    private static boolean checkBodySensors(Context context) throws Throwable {
        SensorManager sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        PermissionTest test = new SensorTest(sensorManager);
        test.test();
        return true;
    }

    private static boolean checkReadSms(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://sms/"), null, null, null, null);
        if (cursor != null) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkReadStorage() throws Throwable {
        File file = new File(Environment.getExternalStorageDirectory().getPath());
        return file.canRead();
    }

    private static boolean checkWriteStorage() throws Throwable {
        File file = Environment.getExternalStorageDirectory();
        return file.canWrite();
    }
}