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
package com.yanzhenjie.permission;

import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission} instead.
 */
@Deprecated
public final class Permission {

    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#READ_CALENDAR} instead.
     */
    @Deprecated
    public static final String READ_CALENDAR = "android.permission.READ_CALENDAR";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#WRITE_CALENDAR} instead.
     */
    @Deprecated
    public static final String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";

    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#CAMERA} instead.
     */
    @Deprecated
    public static final String CAMERA = "android.permission.CAMERA";

    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#READ_CONTACTS} instead.
     */
    @Deprecated
    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#WRITE_CONTACTS} instead.
     */
    @Deprecated
    public static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#GET_ACCOUNTS} instead.
     */
    @Deprecated
    public static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";

    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#ACCESS_FINE_LOCATION} instead.
     */
    @Deprecated
    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#ACCESS_COARSE_LOCATION} instead.
     */
    @Deprecated
    public static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";

    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#RECORD_AUDIO} instead.
     */
    @Deprecated
    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";

    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#READ_PHONE_STATE} instead.
     */
    @Deprecated
    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#CALL_PHONE} instead.
     */
    @Deprecated
    public static final String CALL_PHONE = "android.permission.CALL_PHONE";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#READ_CALL_LOG} instead.
     */
    @Deprecated
    public static final String READ_CALL_LOG = "android.permission.READ_CALL_LOG";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#WRITE_CALL_LOG} instead.
     */
    @Deprecated
    public static final String WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#ADD_VOICEMAIL} instead.
     */
    @Deprecated
    public static final String ADD_VOICEMAIL = "com.android.voicemail.permission.ADD_VOICEMAIL";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#ADD_VOICEMAIL_MANIFEST} instead.
     */
    @Deprecated
    private static final String ADD_VOICEMAIL_MANIFEST = "android.permission.ADD_VOICEMAIL";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#USE_SIP} instead.
     */
    @Deprecated
    public static final String USE_SIP = "android.permission.USE_SIP";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#PROCESS_OUTGOING_CALLS} instead.
     */
    @Deprecated
    public static final String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#READ_PHONE_NUMBERS} instead.
     */
    @Deprecated
    public static final String READ_PHONE_NUMBERS = "android.permission.READ_PHONE_NUMBERS";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#ANSWER_PHONE_CALLS} instead.
     */
    @Deprecated
    public static final String ANSWER_PHONE_CALLS = "android.permission.ANSWER_PHONE_CALLS";

    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#BODY_SENSORS} instead.
     */
    @Deprecated
    public static final String BODY_SENSORS = "android.permission.BODY_SENSORS";

    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#SEND_SMS} instead.
     */
    @Deprecated
    public static final String SEND_SMS = "android.permission.SEND_SMS";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#RECEIVE_SMS} instead.
     */
    @Deprecated
    public static final String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#READ_SMS} instead.
     */
    @Deprecated
    public static final String READ_SMS = "android.permission.READ_SMS";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#RECEIVE_WAP_PUSH} instead.
     */
    @Deprecated
    public static final String RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#RECEIVE_MMS} instead.
     */
    @Deprecated
    public static final String RECEIVE_MMS = "android.permission.RECEIVE_MMS";

    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#READ_EXTERNAL_STORAGE} instead.
     */
    @Deprecated
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#WRITE_EXTERNAL_STORAGE} instead.
     */
    @Deprecated
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    /**
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission.Group} instead.
     */
    @Deprecated
    public static final class Group {

        /**
         * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission.Group#CALENDAR} instead.
         */
        @Deprecated
        public static final String[] CALENDAR = new String[] {Permission.READ_CALENDAR, Permission.WRITE_CALENDAR};

        /**
         * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission.Group#CAMERA} instead.
         */
        @Deprecated
        public static final String[] CAMERA = new String[] {Permission.CAMERA};

        /**
         * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission.Group#CONTACTS} instead.
         */
        @Deprecated
        public static final String[] CONTACTS = new String[] {Permission.READ_CONTACTS, Permission.WRITE_CONTACTS,
            Permission.GET_ACCOUNTS};

        /**
         * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission.Group#LOCATION} instead.
         */
        @Deprecated
        public static final String[] LOCATION = new String[] {Permission.ACCESS_FINE_LOCATION,
            Permission.ACCESS_COARSE_LOCATION};

        /**
         * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission.Group#MICROPHONE} instead.
         */
        @Deprecated
        public static final String[] MICROPHONE = new String[] {Permission.RECORD_AUDIO};

        /**
         * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission.Group#PHONE} instead.
         */
        @Deprecated
        public static final String[] PHONE;

        static {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PHONE = new String[] {Permission.READ_PHONE_STATE, Permission.CALL_PHONE, Permission.READ_CALL_LOG,
                    Permission.WRITE_CALL_LOG, Permission.ADD_VOICEMAIL, Permission.USE_SIP,
                    Permission.PROCESS_OUTGOING_CALLS, Permission.READ_PHONE_NUMBERS, Permission.ANSWER_PHONE_CALLS};
            } else {
                PHONE = new String[] {Permission.READ_PHONE_STATE, Permission.CALL_PHONE, Permission.READ_CALL_LOG,
                    Permission.WRITE_CALL_LOG, Permission.ADD_VOICEMAIL, Permission.USE_SIP,
                    Permission.PROCESS_OUTGOING_CALLS};
            }
        }

        /**
         * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission.Group#SENSORS} instead.
         */
        @Deprecated
        public static final String[] SENSORS = new String[] {Permission.BODY_SENSORS};

        /**
         * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission.Group#SMS} instead.
         */
        @Deprecated
        public static final String[] SMS = new String[] {Permission.SEND_SMS, Permission.RECEIVE_SMS,
            Permission.READ_SMS, Permission.RECEIVE_WAP_PUSH, Permission.RECEIVE_MMS};

        /**
         * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission.Group#STORAGE} instead.
         */
        @Deprecated
        public static final String[] STORAGE = new String[] {Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE};
    }

    /**
     * Turn permissions into text.
     *
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#transformText(Context, String...)} instead.
     */
    @Deprecated
    public static List<String> transformText(Context context, String... permissions) {
        return transformText(context, Arrays.asList(permissions));
    }

    /**
     * Turn permissions into text.
     *
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#transformText(Context, String[]...)} instead.
     */
    @Deprecated
    public static List<String> transformText(Context context, String[]... groups) {
        List<String> permissionList = new ArrayList<>();
        for (String[] group : groups) {
            permissionList.addAll(Arrays.asList(group));
        }
        return transformText(context, permissionList);
    }

    /**
     * Turn permissions into text.
     *
     * @deprecated use {@link com.yanzhenjie.permission.runtime.Permission#transformText(Context, List)} instead.
     */
    @Deprecated
    public static List<String> transformText(Context context, List<String> permissions) {
        List<String> textList = new ArrayList<>();
        for (String permission : permissions) {
            switch (permission) {
                case Permission.READ_CALENDAR:
                case Permission.WRITE_CALENDAR: {
                    String message = context.getString(R.string.permission_name_calendar);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }

                case Permission.CAMERA: {
                    String message = context.getString(R.string.permission_name_camera);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.READ_CONTACTS:
                case Permission.WRITE_CONTACTS: {
                    String message = context.getString(R.string.permission_name_contacts);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.GET_ACCOUNTS: {
                    String message = context.getString(R.string.permission_name_accounts);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.ACCESS_FINE_LOCATION:
                case Permission.ACCESS_COARSE_LOCATION: {
                    String message = context.getString(R.string.permission_name_location);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.RECORD_AUDIO: {
                    String message = context.getString(R.string.permission_name_microphone);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.READ_PHONE_STATE:
                case Permission.CALL_PHONE:
                case Permission.READ_CALL_LOG:
                case Permission.WRITE_CALL_LOG:
                case Permission.ADD_VOICEMAIL:
                case Permission.ADD_VOICEMAIL_MANIFEST:
                case Permission.USE_SIP:
                case Permission.PROCESS_OUTGOING_CALLS:
                case Permission.READ_PHONE_NUMBERS:
                case Permission.ANSWER_PHONE_CALLS: {
                    String message = context.getString(R.string.permission_name_phone);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.BODY_SENSORS: {
                    String message = context.getString(R.string.permission_name_sensors);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.SEND_SMS:
                case Permission.RECEIVE_SMS:
                case Permission.READ_SMS:
                case Permission.RECEIVE_WAP_PUSH:
                case Permission.RECEIVE_MMS: {
                    String message = context.getString(R.string.permission_name_sms);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.READ_EXTERNAL_STORAGE:
                case Permission.WRITE_EXTERNAL_STORAGE: {
                    String message = context.getString(R.string.permission_name_storage);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
            }
        }
        return textList;
    }
}