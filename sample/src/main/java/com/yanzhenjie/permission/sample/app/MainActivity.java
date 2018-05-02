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
package com.yanzhenjie.permission.sample.app;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;
import com.yanzhenjie.permission.sample.InstallRationale;
import com.yanzhenjie.permission.sample.R;
import com.yanzhenjie.permission.sample.RuntimeRationale;

import java.io.File;
import java.util.List;

/**
 * Created by Yan Zhenjie on 2016/9/17.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
        findViewById(R.id.btn_setting).setOnClickListener(this);

        findViewById(R.id.btn_install).setOnClickListener(this);
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
            case R.id.btn_setting: {
                setPermission();
                break;
            }
            case R.id.btn_install: {
                requestPermissionForInstallPackage();
                break;
            }
        }
    }

    /**
     * Request permissions.
     */
    private void requestPermission(String... permissions) {
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        toast(R.string.successfully);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        toast(R.string.failure);
                        if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                            showSettingDialog(MainActivity.this, permissions);
                        }
                    }
                })
                .start();
    }

    /**
     * Display setting dialog.
     */
    public void showSettingDialog(Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    /**
     * Set permissions.
     */
    private void setPermission() {
        AndPermission.with(this)
                .runtime()
                .setting()
                .onComeback(new Setting.Action() {
                    @Override
                    public void onAction() {
                        Toast.makeText(MainActivity.this, R.string.message_setting_comeback, Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

    /**
     * Request to read and write external storage permissions.
     */
    private void requestPermissionForInstallPackage() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        new WriteApkTask(MainActivity.this, new Runnable() {
                            @Override
                            public void run() {
                                installPackage();
                            }
                        }).execute();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        toast(R.string.message_install_storage_failed);
                    }
                })
                .start();
    }

    /**
     * Install package.
     */
    private void installPackage() {
        AndPermission.with(this)
                .install()
                .file(new File(Environment.getExternalStorageDirectory(), "android.apk"))
                .rationale(new InstallRationale())
                .onGranted(new Action<File>() {
                    @Override
                    public void onAction(File data) {
                        // Installing.
                    }
                })
                .onDenied(new Action<File>() {
                    @Override
                    public void onAction(File data) {
                        // The user refused to install.
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