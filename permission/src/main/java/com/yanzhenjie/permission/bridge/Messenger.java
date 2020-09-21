/*
 * Copyright © 2018 Zhenjie Yan.
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.yanzhenjie.permission.AndPermission;

/**
 * Created by Zhenjie Yan on 2018/6/9.
 */
class Messenger extends BroadcastReceiver {

    public static void send(Context context, String suffix) {
        Intent broadcast = new Intent(AndPermission.bridgeAction(context, suffix));
        context.sendBroadcast(broadcast);
    }

    private final Context mContext;
    private final Callback mCallback;
    private boolean mIsRegistered;

    public Messenger(Context context, Callback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    public void register(String suffix) {
        if (!mIsRegistered) {
            IntentFilter filter = new IntentFilter(AndPermission.bridgeAction(mContext, suffix));
            mContext.registerReceiver(this, filter);
            mIsRegistered = true;
        }
    }

    /*
    Fix exception:
    Caused by: java.lang.IllegalArgumentException: Receiver not registered: com.yanzhenjie.permission.bridge.Messenger@c949a97
    at android.app.LoadedApk.forgetReceiverDispatcher(LoadedApk.java:1471)
    at android.app.ContextImpl.unregisterReceiver(ContextImpl.java:1571)
    at android.content.ContextWrapper.unregisterReceiver(ContextWrapper.java:664)
    at com.yanzhenjie.permission.bridge.Messenger.unRegister(SourceFile:1)
    at com.yanzhenjie.permission.bridge.RequestExecutor.onCallback(SourceFile:2)
    at com.yanzhenjie.permission.bridge.Messenger.onReceive(SourceFile:1)
    at android.app.LoadedApk$ReceiverDispatcher$Args.lambda$getRunnable$0$LoadedApk$ReceiverDispatcher$Args(LoadedApk.java:1620)
     */
    public void unRegister() {
        if (mIsRegistered) {
            mContext.unregisterReceiver(this);
            mIsRegistered = false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mCallback.onCallback();
    }

    public interface Callback {

        void onCallback();
    }
}