package com.yanzhenjie.permission.media.capture;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.source.Source;

/**
 * Created by Khanh Bui on 2022/2/22.
 */
abstract class BaseRequest implements CaptureRequest {

    protected Source mSource;

    private Action<Intent> mGranted;
    private Action<Void> mDenied;

    public BaseRequest(Source source) {
        this.mSource = source;
    }

    @Override
    public CaptureRequest onGranted(@NonNull Action<Intent> granted) {
        this.mGranted = granted;
        return this;
    }

    @Override
    public CaptureRequest onDenied(@NonNull Action<Void> denied) {
        this.mDenied = denied;
        return this;
    }

    /**
     * Callback acceptance status.
     */
    final void callbackSucceed(Intent intent) {
        if (mGranted != null) {
            mGranted.onAction(intent);
        }
    }

    /**
     * Callback rejected state.
     */
    final void callbackFailed() {
        if (mDenied != null) {
            mDenied.onAction(null);
        }
    }
}
