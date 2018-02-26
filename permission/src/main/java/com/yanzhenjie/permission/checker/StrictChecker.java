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

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.yanzhenjie.permission.Permission;

import java.util.List;

/**
 * Created by YanZhenjie on 2018/1/7.
 */
public final class StrictChecker implements PermissionChecker {

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
                    return true;
                case Permission.ACCESS_COARSE_LOCATION:
                case Permission.ACCESS_FINE_LOCATION:
                    return checkLocation(context);
                case Permission.RECORD_AUDIO:
                    return checkRecordAudio();
                case Permission.READ_PHONE_STATE:
                    return checkReadPhoneState(context);
                case Permission.CALL_PHONE:
                    return true;
                case Permission.READ_CALL_LOG:
                    return checkReadCallLog(context);
                case Permission.WRITE_CALL_LOG:
                    return checkWriteCallLog(context);
                case Permission.ADD_VOICEMAIL:
                    return checkAddVoicemail(context);
                case Permission.USE_SIP:
                    return checkSip(context);
                case Permission.PROCESS_OUTGOING_CALLS:
                    return true;
                case Permission.BODY_SENSORS:
                    return checkSensors(context);
                case Permission.SEND_SMS:
                case Permission.RECEIVE_MMS:
                    return true;
                case Permission.READ_SMS:
                    return checkReadSms(context);
                case Permission.RECEIVE_WAP_PUSH:
                case Permission.RECEIVE_SMS:
                    return true;
                case Permission.READ_EXTERNAL_STORAGE:
                    return checkReadStorage();
                case Permission.WRITE_EXTERNAL_STORAGE:
                    return checkWriteStorage();
            }
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

    private static boolean checkReadCalendar(Context context) throws Throwable {
        PermissionTest test = new CalendarReadTest(context);
        return test.test();
    }

    private static boolean checkWriteCalendar(Context context) throws Throwable {
        PermissionTest test = new CalendarWriteTest(context);
        return test.test();
    }

    private static boolean checkCamera(Context context) throws Throwable {
        PermissionTest test = new CameraTest(context);
        return test.test();
    }

    private static boolean checkReadContacts(Context context) throws Throwable {
        PermissionTest test = new ContactsReadTest(context);
        return test.test();
    }

    private static boolean checkWriteContacts(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        PermissionTest test = new ContactsWriteTest(resolver);
        return test.test();
    }

    private static boolean checkLocation(Context context) throws Throwable {
        PermissionTest test = new LocationTest(context);
        return test.test();
    }

    private static boolean checkRecordAudio() throws Throwable {
        PermissionTest test = new RecordAudioTest();
        return test.test();
    }

    private static boolean checkReadPhoneState(Context context) throws Throwable {
        PermissionTest test = new PhoneStateReadTest(context);
        return test.test();
    }

    private static boolean checkReadCallLog(Context context) throws Throwable {
        PermissionTest test = new CallLogReadTest(context);
        return test.test();
    }

    private static boolean checkWriteCallLog(Context context) throws Throwable {
        PermissionTest test = new CallLogWriteTest(context);
        return test.test();
    }

    private static boolean checkAddVoicemail(Context context) throws Throwable {
        PermissionTest test = new AddVoicemailTest(context);
        return test.test();
    }

    private static boolean checkSip(Context context) throws Throwable {
        PermissionTest test = new SipTest(context);
        return test.test();
    }

    private static boolean checkSensors(Context context) throws Throwable {
        PermissionTest test = new SensorsTest(context);
        return test.test();
    }

    private static boolean checkReadSms(Context context) throws Throwable {
        PermissionTest test = new SmsReadTest(context);
        return test.test();
    }

    private static boolean checkReadStorage() throws Throwable {
        PermissionTest test = new StorageReadTest();
        return test.test();
    }

    private static boolean checkWriteStorage() throws Throwable {
        PermissionTest test = new StorageWriteTest();
        return test.test();
    }
}
