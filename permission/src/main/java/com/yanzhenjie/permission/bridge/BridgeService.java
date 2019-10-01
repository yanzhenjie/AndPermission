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
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.yanzhenjie.permission.source.ContextSource;
import com.yanzhenjie.permission.source.Source;

/**
 * Created by Zhenjie Yan on 2019-08-30.
 */
public class BridgeService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mStub.asBinder();
    }

    private IBridge.Stub mStub = new IBridge.Stub() {

        private Source mSource = new ContextSource(BridgeService.this);

        @Override
        public void requestAppDetails(String suffix) throws RemoteException {
            BridgeActivity.requestAppDetails(mSource, suffix);
        }

        @Override
        public void requestPermission(String suffix, String[] permissions) throws RemoteException {
            BridgeActivity.requestPermission(mSource, suffix, permissions);
        }

        @Override
        public void requestInstall(String suffix) throws RemoteException {
            BridgeActivity.requestInstall(mSource, suffix);
        }

        @Override
        public void requestOverlay(String suffix) throws RemoteException {
            BridgeActivity.requestOverlay(mSource, suffix);
        }

        @Override
        public void requestAlertWindow(String suffix) throws RemoteException {
            BridgeActivity.requestAlertWindow(mSource, suffix);
        }

        @Override
        public void requestNotify(String suffix) throws RemoteException {
            BridgeActivity.requestNotify(mSource, suffix);
        }

        @Override
        public void requestNotificationListener(String suffix) throws RemoteException {
            BridgeActivity.requestNotificationListener(mSource, suffix);
        }

        @Override
        public void requestWriteSetting(String suffix) throws RemoteException {
            BridgeActivity.requestWriteSetting(mSource, suffix);
        }
    };
}