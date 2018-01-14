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

import java.util.UUID;

/**
 * Created by YanZhenjie on 2018/1/14.
 */
class ContactsWriteTest implements PermissionTest {

    private ContentResolver mResolver;
    private String mDisplayName;

    ContactsWriteTest(ContentResolver resolver) {
        this.mResolver = resolver;
        mDisplayName = UUID.randomUUID().toString();
    }

    @Override
    public void test() throws Throwable {
        try {
            ContentValues values = new ContentValues();
            Uri rawContactUri = mResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values);
            values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, ContentUris.parseId(rawContactUri));
            values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, mDisplayName);
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "1");
            mResolver.insert(ContactsContract.Data.CONTENT_URI, values);
        } finally {
            delete();
        }
    }

    private void delete() {
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cursor = mResolver.query(uri, new String[]{ContactsContract.Contacts.Data._ID}, "display_name=?", new String[]{mDisplayName}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                mResolver.delete(uri, "display_name=?", new String[]{mDisplayName});
                uri = Uri.parse("content://com.android.contacts/data");
                mResolver.delete(uri, "raw_contact_id=?", new String[]{Integer.toString(id)});
            }
            cursor.close();
        }
    }
}