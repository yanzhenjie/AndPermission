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

    public static void send(Context context, String suffix, Intent extraIntent) {
        Intent broadcast = new Intent(AndPermission.bridgeAction(context, suffix));
        broadcast.putExtra(Intent.EXTRA_INTENT, extraIntent);
        context.sendBroadcast(broadcast);
    }

    private final Context mContext;
    private final Callback mCallback;

    public Messenger(Context context, Callback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    public void register(String suffix) {
        IntentFilter filter = new IntentFilter(AndPermission.bridgeAction(mContext, suffix));
        mContext.registerReceiver(this, filter);
    }

    public void unRegister() {
        mContext.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent extraIntent = intent.getParcelableExtra(Intent.EXTRA_INTENT);
        mCallback.onCallback(extraIntent);
    }

    public interface Callback {

        void onCallback(Intent intent);
    }
}