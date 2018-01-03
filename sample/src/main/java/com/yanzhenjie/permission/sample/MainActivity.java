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
package com.yanzhenjie.permission.sample;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

/**
 * Created by Yan Zhenjie on 2016/9/17.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RationaleListener mRationaleListener;
    private PermissionSetting mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.btn_request_camera).setOnClickListener(this);
        findViewById(R.id.btn_request_contact_location).setOnClickListener(this);
        findViewById(R.id.btn_request_calendar).setOnClickListener(this);
        findViewById(R.id.btn_request__microphone_storage).setOnClickListener(this);
        findViewById(R.id.btn_request_phone).setOnClickListener(this);
        findViewById(R.id.btn_request_sensors_sms).setOnClickListener(this);

        mRationaleListener = new DefaultRationale();
        mSetting = new PermissionSetting(this);

        TextView tvMessage = (TextView) findViewById(R.id.tv_message);
        tvMessage.setText("品牌字符串：" + Build.MANUFACTURER);

        findViewById(R.id.btn_request_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.permissionSetting(MainActivity.this)
                        .execute();
            }
        });
        findViewById(R.id.btn_request_setting_window).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.alertWindowSetting(MainActivity.this)
                        .execute();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_camera: {
                AndPermission.with(this)
                        .permission(Permission.CAMERA)
                        .rationale(mRationaleListener)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                toast(R.string.successfully);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                toast(R.string.failure);
                                if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                                    mSetting.showSetting(permissions);
                                }
                            }
                        })
                        .start();
                break;
            }
            case R.id.btn_request_contact_location: {
                AndPermission.with(this)
                        .permission(
                                Permission.Group.CONTACTS,
                                Permission.Group.LOCATION
                        )
                        .rationale(mRationaleListener)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                toast(R.string.successfully);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                toast(R.string.failure);
                                if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                                    mSetting.showSetting(permissions);
                                }
                            }
                        })
                        .start();
                break;
            }
            case R.id.btn_request_calendar: {
                AndPermission.with(this)
                        .permission(Permission.Group.CALENDAR)
                        .rationale(mRationaleListener)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                toast(R.string.successfully);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                toast(R.string.failure);
                                if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                                    mSetting.showSetting(permissions);
                                }
                            }
                        })
                        .start();
                break;
            }
            case R.id.btn_request__microphone_storage: {
                AndPermission.with(this)
                        .permission(
                                Permission.Group.MICROPHONE,
                                Permission.Group.STORAGE
                        )
                        .rationale(mRationaleListener)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                toast(R.string.successfully);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                toast(R.string.failure);
                                if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                                    mSetting.showSetting(permissions);
                                }
                            }
                        })
                        .start();
                break;
            }
            case R.id.btn_request_phone: {
                AndPermission.with(this)
                        .permission(Permission.Group.PHONE)
                        .rationale(mRationaleListener)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                toast(R.string.successfully);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                toast(R.string.failure);
                                if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                                    mSetting.showSetting(permissions);
                                }
                            }
                        })
                        .start();
                break;
            }
            case R.id.btn_request_sensors_sms: {
                AndPermission.with(this)
                        .permission(
                                Permission.Group.SMS,
                                Permission.Group.SENSORS
                        )
                        .rationale(mRationaleListener)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                toast(R.string.successfully);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                toast(R.string.failure);
                                if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                                    mSetting.showSetting(permissions);
                                }
                            }
                        })
                        .start();
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }

    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
