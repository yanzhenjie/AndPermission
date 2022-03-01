package com.yanzhenjie.permission.sample.app;

import static android.app.Activity.RESULT_OK;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.yanzhenjie.permission.sample.R;

/**
 * Created by Khanh Bui on 2022/2/22.
 */
public class ScreenCaptureService extends Service {

    private static final String TAG = ScreenCaptureService.class.getSimpleName();

    private static final String CHANNEL_ID = "001";
    private static final String CHANNEL_NAME = "ScreenCapture";

    public static final String DATA = "data";

    private MediaProjection mMediaProjection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();

        Intent resultData = intent.getParcelableExtra(ScreenCaptureService.DATA);
        initMediaProjection(resultData);

        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDestroy() {
        stopForeground(true);

        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }

        super.onDestroy();
    }

    private void startForeground() {
        createNotificationChannel();

        Notification notification = createNotification();
        startForeground(101, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification() {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }
        builder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.permission_screen_capture))
                .setContentText(getString(R.string.permission_screen_capture_desc));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return builder.build();
        } else {
            return builder.getNotification();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initMediaProjection(Intent resultData) {
        MediaProjectionManager pm = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mMediaProjection = pm.getMediaProjection(RESULT_OK, resultData);

        if (mMediaProjection == null) {
            Log.w(TAG, "Cannot retrieve MediaProjection.");
        } else {
            Log.i(TAG, "MediaProjection started.");
        }
    }
}
