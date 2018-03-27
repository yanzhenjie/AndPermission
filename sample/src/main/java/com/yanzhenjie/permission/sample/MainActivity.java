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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;

import java.util.List;

/**
 * Created by Yan Zhenjie on 2016/9/17.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Rationale mRationale;
    private PermissionSetting mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
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

        findViewById(R.id.btn_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.permissionSetting(MainActivity.this)
                        .execute();
            }
        });

        mRationale = new DefaultRationale();
        mSetting = new PermissionSetting(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_camera: {
                requestPermission(Permission.Group.CAMERA);
                break;
            }
            case R.id.btn_request_contact: {
                PopupMenu popupMenu = createMenu(v, getResources().getStringArray(R.array.contacts));
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int order = item.getItemId();
                        switch (order) {
                            case 0: {
                                requestPermission(Permission.READ_CONTACTS);
                                break;
                            }
                            case 1: {
                                requestPermission(Permission.WRITE_CONTACTS);
                                break;
                            }
                            case 2: {
                                requestPermission(Permission.GET_ACCOUNTS);
                                break;
                            }
                            case 3: {
                                requestPermission(Permission.Group.CONTACTS);
                                break;
                            }
                        }
                        return true;
                    }
                });
                popupMenu.show();
                break;
            }
            case R.id.btn_request_location: {
                requestPermission(Permission.Group.LOCATION);
                break;
            }
            case R.id.btn_request_calendar: {
                PopupMenu popupMenu = createMenu(v, getResources().getStringArray(R.array.calendar));
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int order = item.getOrder();
                        switch (order) {
                            case 0: {
                                requestPermission(Permission.READ_CALENDAR);
                                break;
                            }
                            case 1: {
                                requestPermission(Permission.WRITE_CALENDAR);
                                break;
                            }
                            case 2: {
                                requestPermission(Permission.Group.CALENDAR);
                                break;
                            }
                        }
                        return true;
                    }
                });
                popupMenu.show();
                break;
            }
            case R.id.btn_request_microphone: {
                requestPermission(Permission.Group.MICROPHONE);
                break;
            }
            case R.id.btn_request_storage: {
                PopupMenu popupMenu = createMenu(v, getResources().getStringArray(R.array.storage));
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int order = item.getOrder();
                        switch (order) {
                            case 0: {
                                requestPermission(Permission.READ_EXTERNAL_STORAGE);
                                break;
                            }
                            case 1: {
                                requestPermission(Permission.WRITE_EXTERNAL_STORAGE);
                                break;
                            }
                            case 2: {
                                requestPermission(Permission.Group.STORAGE);
                                break;
                            }
                        }
                        return true;
                    }
                });
                popupMenu.show();
                break;
            }
            case R.id.btn_request_phone: {
                PopupMenu popupMenu = createMenu(v, getResources().getStringArray(R.array.phone));
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int order = item.getOrder();
                        switch (order) {
                            case 0: {
                                requestPermission(Permission.READ_PHONE_STATE);
                                break;
                            }
                            case 1: {
                                requestPermission(Permission.CALL_PHONE);
                                break;
                            }
                            case 2: {
                                requestPermission(Permission.READ_CALL_LOG);
                                break;
                            }
                            case 3: {
                                requestPermission(Permission.WRITE_CALL_LOG);
                                break;
                            }
                            case 4: {
                                requestPermission(Permission.ADD_VOICEMAIL);
                                break;
                            }
                            case 5: {
                                requestPermission(Permission.USE_SIP);
                                break;
                            }
                            case 6: {
                                requestPermission(Permission.PROCESS_OUTGOING_CALLS);
                                break;
                            }
                            case 7: {
                                requestPermission(Permission.Group.PHONE);
                                break;
                            }
                        }
                        return true;
                    }
                });
                popupMenu.show();
                break;
            }
            case R.id.btn_request_sensors: {
                requestPermission(Permission.Group.SENSORS);
                break;
            }
            case R.id.btn_request_sms: {
                PopupMenu popupMenu = createMenu(v, getResources().getStringArray(R.array.sms));
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int order = item.getOrder();
                        switch (order) {
                            case 0: {
                                requestPermission(Permission.SEND_SMS);
                                break;
                            }
                            case 1: {
                                requestPermission(Permission.RECEIVE_SMS);
                                break;
                            }
                            case 2: {
                                requestPermission(Permission.READ_SMS);
                                break;
                            }
                            case 3: {
                                requestPermission(Permission.RECEIVE_WAP_PUSH);
                                break;
                            }
                            case 4: {
                                requestPermission(Permission.RECEIVE_MMS);
                                break;
                            }
                            case 5: {
                                requestPermission(Permission.Group.SMS);
                                break;
                            }
                        }
                        return true;
                    }
                });
                popupMenu.show();
                break;
            }
        }
    }

    private void requestPermission(String... permissions) {
        AndPermission.with(this)
                .permission(permissions)
                .rationale(mRationale)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
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
    }

    private void requestPermission(String[]... permissions) {
        AndPermission.with(this)
                .permission(permissions)
                .rationale(mRationale)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
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
    }

    /**
     * Create menu.
     */
    private PopupMenu createMenu(View v, String[] menuArray) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        Menu menu = popupMenu.getMenu();
        for (int i = 0; i < menuArray.length; i++) {
            String menuText = menuArray[i];
            menu.add(0, i, i, menuText);
        }
        return popupMenu;
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