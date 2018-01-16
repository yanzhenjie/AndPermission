package com.yanzhenjie.permission.checker;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by YanZhenjie on 2018/1/15.
 */
class CameraTest implements PermissionTest {

    private SurfaceHolder mHolder;

    CameraTest(Context context) {
        SurfaceView surfaceView = new SurfaceView(context);
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(CALLBACK);
    }

    @Override
    public boolean test() throws Throwable {
        Camera camera = null;
        try {
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            camera.setParameters(parameters);
            camera.setPreviewDisplay(mHolder);
            camera.setPreviewCallback(PREVIEW_CALLBACK);
            camera.startPreview();
            return true;
        } finally {
            if (camera != null) {
                camera.stopPreview();
                camera.setPreviewDisplay(null);
                camera.setPreviewCallback(null);
                camera.release();
            }
        }
    }

    private static final Camera.PreviewCallback PREVIEW_CALLBACK = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
        }
    };

    private static final SurfaceHolder.Callback CALLBACK = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };
}
