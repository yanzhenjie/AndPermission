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
package com.yanzhenjie.permission;

import android.util.Log;

/**
 * Created by YanZhenjie on 2018/1/14.
 */
public class ApLog {

    private static String sTag = "AndPermission";
    private static boolean sEnable = false;

    public static void setEnable(boolean enable) {
        ApLog.sEnable = enable;
    }

    public static void setTag(String tag) {
        ApLog.sTag = tag;
    }

    public static void v(String message) {
        if (sEnable)
            Log.v(sTag, message);
    }

    public static void v(Throwable e) {
        if (sEnable)
            Log.v(sTag, "", e);
    }

    public static void v(String message, Throwable e) {
        if (sEnable)
            Log.v(sTag, message, e);
    }

    public static void i(String message) {
        if (sEnable)
            Log.i(sTag, message);
    }

    public static void i(Throwable e) {
        if (sEnable)
            Log.i(sTag, "", e);
    }

    public static void i(String message, Throwable e) {
        if (sEnable)
            Log.i(sTag, message, e);
    }

    public static void d(String message) {
        if (sEnable)
            Log.d(sTag, message);
    }

    public static void d(Throwable e) {
        if (sEnable)
            Log.d(sTag, "", e);
    }

    public static void d(String message, Throwable e) {
        if (sEnable)
            Log.d(sTag, message, e);
    }

    public static void w(String message) {
        if (sEnable)
            Log.w(sTag, message);
    }

    public static void w(Throwable e) {
        if (sEnable)
            Log.w(sTag, "", e);
    }

    public static void w(String message, Throwable e) {
        if (sEnable)
            Log.w(sTag, message, e);
    }

    public static void e(String message) {
        if (sEnable)
            Log.e(sTag, message);
    }

    public static void e(Throwable e) {
        if (sEnable)
            Log.e(sTag, "", e);
    }

    public static void e(String message, Throwable e) {
        if (sEnable)
            Log.e(sTag, message, e);
    }
}