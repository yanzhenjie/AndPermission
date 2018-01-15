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
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by YanZhenjie on 2018/1/14.
 */
class ContactsWriteTest implements PermissionTest {

    private static final String DISPLAY_NAME = "PERMISSION";

    private ContentResolver mResolver;

    ContactsWriteTest(ContentResolver resolver) {
        this.mResolver = resolver;
    }

    @Override
    public void test() throws Throwable {
        Cursor cursor = mResolver.query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data._ID},
                ContactsContract.Data.DISPLAY_NAME + "=?",
                new String[]{DISPLAY_NAME},
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                long dataId = cursor.getLong(0);
                cursor.close();
                update(dataId);
            } else {
                cursor.close();
                insert();
            }
        }
    }

    private void insert() {
        ContentValues values = new ContentValues();
        Uri contractUri = mResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long contactId = ContentUris.parseId(contractUri);

        values.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);
        values.put(ContactsContract.Data.DATA1, DISPLAY_NAME);
        values.put(ContactsContract.Data.DATA2, DISPLAY_NAME);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        mResolver.insert(ContactsContract.Data.CONTENT_URI, values);

//        Uri resourceUri = mResolver.insert(ContactsContract.Data.CONTENT_URI, values);
//        long dataId = ContentUris.parseId(resourceUri);
//        delete(contactId, dataId);
    }

    private void delete(long contactId, long dataId) {
        mResolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts._ID + "=?", new String[]{Long.toString(contactId)});
        mResolver.delete(ContactsContract.Data.CONTENT_URI, ContactsContract.Data._ID + "=?", new String[]{Long.toString(dataId)});
    }

    private void update(long dataId) {
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Data.DATA1, DISPLAY_NAME);
        values.put(ContactsContract.Data.DATA2, DISPLAY_NAME);
        mResolver.update(ContactsContract.Data.CONTENT_URI,
                values,
                ContactsContract.Data._ID + "=? and " + ContactsContract.Data.MIMETYPE + "=?",
                new String[]{Long.toString(dataId), ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE});
    }
}