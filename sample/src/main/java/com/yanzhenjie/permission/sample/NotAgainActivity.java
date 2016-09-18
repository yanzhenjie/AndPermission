/*
 * Copyright 2016 Yan Zhenjie
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
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

/**
 * Created by Yan Zhenjie on 2016/9/18.
 */
public class NotAgainActivity extends AppCompatActivity implements View.OnClickListener, PermissionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_not_again);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.btn_request_single).setOnClickListener(this);
    }

    /**
     * 申请麦克风权限。
     */
    private void requestCameraPermission() {
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.CAMERA)
                .send();
    }

    @Override
    public void onSucceed(int requestCode) {
        Toast.makeText(this, "获取相机权限成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(int requestCode) {
        if (AndPermission.getShouldShowRationalePermissions(this, Manifest.permission.CAMERA)) {
            Toast.makeText(this, "获取相机风权限失败", Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("友好提醒")
                    .setMessage("您已拒绝了相机权限，并且下次不再提示，如果你要继续使用此功能，请在设置中为我们授权相机权限。！")
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
        }

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
        }
    }
}
