/*
 * Copyright © Yan Zhenjie. All Rights Reserved
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
package com.yanzhenjie.permission.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

/**
 * Created by Yan Zhenjie on 2016/9/17.
 */
public class ListenerActivity extends AppCompatActivity implements View.OnClickListener, PermissionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.btn_request_single).setOnClickListener(this);
        findViewById(R.id.btn_request_multi).setOnClickListener(this);
    }

    /**
     * 申请SD卡权限，单个的。
     */
    private void requestCameraPermission() {
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.WRITE_CALENDAR)
                .send();
    }

    @Override
    public void onSucceed(int requestCode) {
        if (requestCode == 100) {
            Toast.makeText(this, "获取日历权限成功", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 101) {
            Toast.makeText(this, "获取联系人、短信、SD卡权限成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailed(int requestCode) {
        if (requestCode == 100) {
            Toast.makeText(this, "获取日历权限失败", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 101) {
            Toast.makeText(this, "获取联系人、短信、SD卡权限失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 申请联系人、短信、权限。
     */
    private void requestContactSMSPermission() {
        AndPermission.with(this)
                .requestCode(101)
                .permission(Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .send();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 这个Activity中没有Fragment，这句话可以注释。
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 没有Listener，最后的PermissionListener参数不写。
//        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

        // 有Listener，最后需要写PermissionListener参数。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_single: {
                requestCameraPermission();
                break;
            }
            case R.id.btn_request_multi: {
                requestContactSMSPermission();
                break;
            }
        }
    }
}
