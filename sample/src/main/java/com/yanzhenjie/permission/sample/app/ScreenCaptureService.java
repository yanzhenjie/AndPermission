package com.yanzhenjie.permission.sample.app;

import static android.app.Activity.RESULT_OK;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Created by Khanh Bui on 2022/2/22.
 */
public class ScreenCaptureService extends Service {

    private static final String TAG = ScreenCaptureService.class.getSimpleName();

    private MediaProjection mMediaProjection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaProjectionManager pm = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mMediaProjection = pm.getMediaProjection(RESULT_OK, intent);

        if (mMediaProjection != null) {
            Log.e(TAG, "Cannot retrieve MediaProjection.");
        } else {
            Log.e(TAG, "MediaProjection started.");
        }

        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDestroy() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }

        super.onDestroy();
    }
}
