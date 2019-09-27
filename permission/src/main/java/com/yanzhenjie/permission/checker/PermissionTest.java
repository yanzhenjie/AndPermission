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

import android.database.Cursor;

/**
 * Created by Zhenjie Yan on 2018/1/14.
 */
interface PermissionTest {

    boolean test() throws Throwable;

    class CursorTest {

        public static void read(Cursor cursor) {
            int count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();
                int type = cursor.getType(0);
                switch (type) {
                    case Cursor.FIELD_TYPE_BLOB:
                    case Cursor.FIELD_TYPE_NULL: {
                        break;
                    }
                    case Cursor.FIELD_TYPE_INTEGER:
                    case Cursor.FIELD_TYPE_FLOAT:
                    case Cursor.FIELD_TYPE_STRING:
                    default: {
                        cursor.getString(0);
                        break;
                    }
                }
            }
        }
    }
}