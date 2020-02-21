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

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;

import androidx.core.content.ContextCompat;

/**
 * Created by Zhenjie Yan on 2018/1/15.
 */
class CameraTest implements PermissionTest {

    private Context mContext;

    CameraTest(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean test() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
            return hasPermission == PackageManager.PERMISSION_GRANTED;
        } else {
            Camera camera = null;
            try {
                int cameraCount = Camera.getNumberOfCameras();
                if (cameraCount <= 0) return true;

                camera = open(cameraCount);
                Camera.Parameters parameters = camera.getParameters();
                camera.setParameters(parameters);
                camera.setPreviewCallback(PREVIEW_CALLBACK);
                camera.startPreview();
                return true;
            } catch (Throwable e) {
                PackageManager packageManager = mContext.getPackageManager();
                return !packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
            } finally {
                if (camera != null) {
                    camera.stopPreview();
                    camera.setPreviewCallback(null);
                    camera.release();
                }
            }
        }
    }

    private static final Camera.PreviewCallback PREVIEW_CALLBACK = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
        }
    };

    public static Camera open(int count) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < count; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return Camera.open(i);
            }
        }
        return Camera.open(0);
    }
}