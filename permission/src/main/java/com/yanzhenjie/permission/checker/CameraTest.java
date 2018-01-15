package com.yanzhenjie.permission.checker;

import android.hardware.Camera;

/**
 * Created by YanZhenjie on 2018/1/15.
 */
class CameraTest implements PermissionTest {

    CameraTest() {
    }

    @Override
    public void test() throws Throwable {
        Camera camera = null;
        try {
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            camera.setParameters(parameters);
        } finally {
            if (camera != null) {
                camera.release();
            }
        }
    }
}
