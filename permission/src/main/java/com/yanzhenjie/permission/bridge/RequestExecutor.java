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

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.yanzhenjie.permission.AndPermission;

import java.util.List;

/**
 * Created by Zhenjie Yan on 2/13/19.
 */
final class RequestExecutor extends Thread implements Messenger.Callback {

    private BridgeRequest mRequest;
    private Messenger mMessenger;

    public RequestExecutor(BridgeRequest request) {
        this.mRequest = request;
    }

    @Override
    public void run() {
        Context context = mRequest.getSource().getContext();

        mMessenger = new Messenger(context, this);
        mMessenger.register(getName());

        Intent intent = new Intent();
        intent.setAction(AndPermission.bridgeAction(context, null));
        intent.setPackage(context.getPackageName());
        context.bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IBridge iBridge = IBridge.Stub.asInterface(iBinder);
            try {
                executeCurrent(iBridge);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    private void executeCurrent(IBridge iBridge) throws RemoteException {
        switch (mRequest.getType()) {
            case BridgeRequest.TYPE_APP_DETAILS: {
                iBridge.requestAppDetails(getName());
                break;
            }
            case BridgeRequest.TYPE_PERMISSION: {
                List<String> permissions = mRequest.getPermissions();
                String[] array = permissions.toArray(new String[0]);
                iBridge.requestPermission(getName(), array);
                break;
            }
            case BridgeRequest.TYPE_INSTALL: {
                iBridge.requestInstall(getName());
                break;
            }
            case BridgeRequest.TYPE_OVERLAY: {
                iBridge.requestOverlay(getName());
                break;
            }
            case BridgeRequest.TYPE_ALERT_WINDOW: {
                iBridge.requestAlertWindow(getName());
                break;
            }
            case BridgeRequest.TYPE_NOTIFY: {
                iBridge.requestNotify(getName());
                break;
            }
            case BridgeRequest.TYPE_NOTIFY_LISTENER: {
                iBridge.requestNotificationListener(getName());
                break;
            }
            case BridgeRequest.TYPE_WRITE_SETTING: {
                iBridge.requestWriteSetting(getName());
                break;
            }
        }
    }


    @Override
    public void onCallback() {
        synchronized (this) {
            mMessenger.unRegister();
            mRequest.getCallback().onCallback();
            mRequest.getSource().getContext().unbindService(mConnection);
            mMessenger = null;
            mRequest = null;
        }
    }
}