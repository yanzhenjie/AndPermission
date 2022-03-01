package com.yanzhenjie.permission.media.capture;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.yanzhenjie.permission.Action;

/**
 * Created by Khanh Bui on 2022/2/22.
 */
public interface CaptureRequest {

    /**
     * Action to be taken when permission is granted.
     */
    CaptureRequest onGranted(@NonNull Action<Intent> granted);

    /**
     * Action to be taken when permission is denied.
     */
    CaptureRequest onDenied(@NonNull Action<Void> denied);

    /**
     * Request permission.
     */
    void start();
}
