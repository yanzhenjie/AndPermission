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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;

import com.yanzhenjie.permission.overlay.setting.AlertWindowSettingPage;
import com.yanzhenjie.permission.overlay.setting.OverlaySettingPage;
import com.yanzhenjie.permission.runtime.setting.RuntimeSettingPage;
import com.yanzhenjie.permission.source.ContextSource;

/**
 * <p>
 * Request permission.
 * </p>
 * Created by Yan Zhenjie on 2017/4/27.
 */
public final class PermissionActivity extends Activity {

    private static final String KEY_INPUT_OPERATION = "KEY_INPUT_OPERATION";
    private static final int VALUE_INPUT_PERMISSION = 1;
    private static final int VALUE_INPUT_PERMISSION_SETTING = 2;
    private static final int VALUE_INPUT_INSTALL = 3;
    private static final int VALUE_INPUT_OVERLAY = 4;
    private static final int VALUE_INPUT_ALERT_WINDOW = 5;

    private static final String KEY_INPUT_PERMISSIONS = "KEY_INPUT_PERMISSIONS";

    private static RequestListener sRequestListener;

    /**
     * Request for permissions.
     */
    public static void requestPermission(Context context, String[] permissions, RequestListener requestListener) {
        PermissionActivity.sRequestListener = requestListener;

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_INPUT_OPERATION, VALUE_INPUT_PERMISSION);
        intent.putExtra(KEY_INPUT_PERMISSIONS, permissions);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for setting.
     */
    public static void permissionSetting(Context context, RequestListener requestListener) {
        PermissionActivity.sRequestListener = requestListener;

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_INPUT_OPERATION, VALUE_INPUT_PERMISSION_SETTING);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for package install.
     */
    public static void requestInstall(Context context, RequestListener requestListener) {
        PermissionActivity.sRequestListener = requestListener;

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_INPUT_OPERATION, VALUE_INPUT_INSTALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for overlay.
     */
    public static void requestOverlay(Context context, RequestListener requestListener) {
        PermissionActivity.sRequestListener = requestListener;

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_INPUT_OPERATION, VALUE_INPUT_OVERLAY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for alert window.
     */
    public static void requestAlertWindow(Context context, RequestListener requestListener) {
        PermissionActivity.sRequestListener = requestListener;

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_INPUT_OPERATION, VALUE_INPUT_ALERT_WINDOW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int operation = intent.getIntExtra(KEY_INPUT_OPERATION, 0);
        switch (operation) {
            case VALUE_INPUT_PERMISSION: {
                String[] permissions = intent.getStringArrayExtra(KEY_INPUT_PERMISSIONS);
                if (permissions != null && sRequestListener != null) {
                    requestPermissions(permissions, VALUE_INPUT_PERMISSION);
                } else {
                    finish();
                }
                break;
            }
            case VALUE_INPUT_PERMISSION_SETTING: {
                if (sRequestListener != null) {
                    RuntimeSettingPage setting = new RuntimeSettingPage(new ContextSource(this));
                    setting.start(VALUE_INPUT_PERMISSION_SETTING);
                } else {
                    finish();
                }
                break;
            }
            case VALUE_INPUT_INSTALL: {
                if (sRequestListener != null) {
                    Intent manageIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    manageIntent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivityForResult(manageIntent, VALUE_INPUT_INSTALL);
                } else {
                    finish();
                }
                break;
            }
            case VALUE_INPUT_OVERLAY: {
                if (sRequestListener != null) {
                    OverlaySettingPage settingPage = new OverlaySettingPage(new ContextSource(this));
                    settingPage.start(VALUE_INPUT_OVERLAY);
                } else {
                    finish();
                }
                break;
            }
            case VALUE_INPUT_ALERT_WINDOW: {
                if (sRequestListener != null) {
                    AlertWindowSettingPage settingPage = new AlertWindowSettingPage(new ContextSource(this));
                    settingPage.start(VALUE_INPUT_ALERT_WINDOW);
                } else {
                    finish();
                }
                break;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (sRequestListener != null) {
            sRequestListener.onRequestCallback();
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (sRequestListener != null) {
            sRequestListener.onRequestCallback();
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        sRequestListener = null;
        super.finish();
    }

    /**
     * permission callback.
     */
    public interface RequestListener {
        void onRequestCallback();
    }
}