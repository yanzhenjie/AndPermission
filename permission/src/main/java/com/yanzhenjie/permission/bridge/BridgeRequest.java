/*
 * Copyright 2019 Zhenjie Yan
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
package com.yanzhenjie.permission.bridge;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.annotation.WorkerThread;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Zhenjie Yan on 2/13/19.
 */
public final class BridgeRequest {

    public static final int TYPE_PERMISSION = 1;
    public static final int TYPE_PERMISSION_SETTING = 2;
    public static final int TYPE_INSTALL = 3;
    public static final int TYPE_OVERLAY = 4;
    public static final int TYPE_ALERT_WINDOW = 5;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_PERMISSION, TYPE_PERMISSION_SETTING, TYPE_INSTALL, TYPE_OVERLAY, TYPE_ALERT_WINDOW})
    private @interface TypeDef {}

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private Context mContext;
    private int mType;
    private String[] mPermissions;

    private BridgeActivity.RequestListener mListener;

    public BridgeRequest(Context context) {
        this.mContext = context;
    }

    public void setType(@TypeDef int type) {
        mType = type;
    }

    public void setPermissions(String[] permissions) {
        mPermissions = permissions;
    }

    public void setListener(BridgeActivity.RequestListener listener) {
        mListener = listener;
    }

    @WorkerThread
    void execute(Object lock) {
        switch (mType) {
            case TYPE_PERMISSION: {
                BridgeActivity.RequestListener listener = new RequestCallback(mListener, lock);
                BridgeActivity.requestPermission(mContext, mPermissions, listener);
                break;
            }
            case TYPE_PERMISSION_SETTING: {
                BridgeActivity.RequestListener listener = new RequestCallback(mListener, lock);
                BridgeActivity.permissionSetting(mContext, listener);
                break;
            }
            case TYPE_INSTALL: {
                BridgeActivity.RequestListener listener = new RequestCallback(mListener, lock);
                BridgeActivity.requestInstall(mContext, listener);
                break;
            }
            case TYPE_OVERLAY: {
                BridgeActivity.RequestListener listener = new RequestCallback(mListener, lock);
                BridgeActivity.requestOverlay(mContext, listener);
                break;
            }
            case TYPE_ALERT_WINDOW: {
                BridgeActivity.RequestListener listener = new RequestCallback(mListener, lock);
                BridgeActivity.requestAlertWindow(mContext, listener);
                break;
            }
        }
    }

    private static final class RequestCallback implements BridgeActivity.RequestListener {

        private final BridgeActivity.RequestListener mListener;
        private final Object mLock;

        public RequestCallback(BridgeActivity.RequestListener listener, Object lock) {
            this.mListener = listener;
            this.mLock = lock;
        }

        @Override
        public void onRequestCallback() {
            if (mListener != null) {
                HANDLER.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (mLock) {
                            mListener.onRequestCallback();
                            mLock.notify();
                        }
                    }
                }, 100);
            } else {
                synchronized (mLock) {
                    mLock.notify();
                }
            }
        }
    }
}