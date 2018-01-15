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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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
        findViewById(R.id.btn_request_contact).setOnClickListener(this);
        findViewById(R.id.btn_request_location).setOnClickListener(this);
        findViewById(R.id.btn_request_calendar).setOnClickListener(this);
        findViewById(R.id.btn_request_microphone).setOnClickListener(this);
        findViewById(R.id.btn_request_storage).setOnClickListener(this);
        findViewById(R.id.btn_request_phone).setOnClickListener(this);
        findViewById(R.id.btn_request_sensors).setOnClickListener(this);
        findViewById(R.id.btn_request_sms).setOnClickListener(this);

        mRationaleListener = new DefaultRationale();
        mSetting = new PermissionSetting(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_camera: {
                requestPermission(Permission.Group.CAMERA, new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        toast(R.string.successfully);
                    }
                });
                break;
            }
            case R.id.btn_request_contact: {
                requestPermission(Permission.Group.CONTACTS, new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        toast(R.string.successfully);
                    }
                });
                break;
            }
            case R.id.btn_request_location: {
                requestPermission(Permission.Group.LOCATION, new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        toast(R.string.successfully);
                    }
                });
                break;
            }
            case R.id.btn_request_calendar: {
                requestPermission(Permission.Group.CALENDAR, new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        toast(R.string.successfully);
                    }
                });
                break;
            }
            case R.id.btn_request_microphone: {
                requestPermission(Permission.Group.MICROPHONE, new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        toast(R.string.successfully);
                    }
                });
                break;
            }
            case R.id.btn_request_storage: {
                requestPermission(Permission.Group.STORAGE, new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        toast(R.string.successfully);
                    }
                });
                break;
            }
            case R.id.btn_request_phone: {
                requestPermission(Permission.Group.PHONE, new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        toast(R.string.successfully);
                    }
                });
                break;
            }
            case R.id.btn_request_sensors: {
                requestPermission(Permission.Group.SENSORS, new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        toast(R.string.successfully);
                    }
                });
                break;
            }
            case R.id.btn_request_sms: {
                requestPermission(Permission.Group.SMS, new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        toast(R.string.successfully);
                    }
                });
                break;
            }
        }
    }

    private void requestPermission(String[] permissions, Action onSucceed) {
        AndPermission.with(this)
                .permission(permissions)
                .rationale(mRationaleListener)
                .onGranted(onSucceed)
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
