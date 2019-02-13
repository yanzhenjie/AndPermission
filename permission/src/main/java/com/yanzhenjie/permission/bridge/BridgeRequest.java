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

    private Context mContext;
    private int mType;
    private String[] mPermissions;
    private Callback mCallback;

    public BridgeRequest(Context context) {
        this.mContext = context;
    }

    public void setType(@TypeDef int type) {
        this.mType = type;
    }

    public void setPermissions(String[] permissions) {
        this.mPermissions = permissions;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @WorkerThread
    void execute(Object lock) {
        MessageCallback callback = new MessageCallback(mCallback, lock);
        Messenger messenger = new Messenger(mContext, callback);
        callback.setMessenger(messenger);
        messenger.register();

        switch (mType) {
            case TYPE_PERMISSION: {
                BridgeActivity.requestPermission(mContext, mPermissions);
                break;
            }
            case TYPE_PERMISSION_SETTING: {
                BridgeActivity.permissionSetting(mContext);
                break;
            }
            case TYPE_INSTALL: {
                BridgeActivity.requestInstall(mContext);
                break;
            }
            case TYPE_OVERLAY: {
                BridgeActivity.requestOverlay(mContext);
                break;
            }
            case TYPE_ALERT_WINDOW: {
                BridgeActivity.requestAlertWindow(mContext);
                break;
            }
        }
    }

    private static final class MessageCallback implements Messenger.Callback {

        private final Callback mCallback;
        private final Object mLock;

        private Messenger mMessenger;

        public MessageCallback(Callback callback, Object lock) {
            this.mCallback = callback;
            this.mLock = lock;
        }

        public void setMessenger(Messenger messenger) {
            this.mMessenger = messenger;
        }

        @Override
        public void onCallback() {
            synchronized (mLock) {
                if (mCallback != null) {
                    mCallback.onCallback();
                }
                if (mMessenger != null) {
                    mMessenger.unRegister();
                }
                mLock.notify();
            }
        }
    }

    public interface Callback {

        void onCallback();
    }
}