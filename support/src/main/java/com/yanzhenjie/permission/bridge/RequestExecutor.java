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

import java.util.concurrent.BlockingQueue;

/**
 * Created by Zhenjie Yan on 2/13/19.
 */
final class RequestExecutor extends Thread implements Messenger.Callback {

    private final BlockingQueue<BridgeRequest> mQueue;
    private BridgeRequest mRequest;
    private Messenger mMessenger;

    public RequestExecutor(BlockingQueue<BridgeRequest> queue) {
        this.mQueue = queue;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    mRequest = mQueue.take();
                } catch (InterruptedException e) {
                    continue;
                }

                mMessenger = new Messenger(mRequest.getSource().getContext(), this);
                mMessenger.register();
                executeCurrent();

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void executeCurrent() {
        switch (mRequest.getType()) {
            case BridgeRequest.TYPE_APP_DETAILS: {
                BridgeActivity.requestAppDetails(mRequest.getSource());
                break;
            }
            case BridgeRequest.TYPE_PERMISSION: {
                BridgeActivity.requestPermission(mRequest.getSource(), mRequest.getPermissions());
                break;
            }
            case BridgeRequest.TYPE_INSTALL: {
                BridgeActivity.requestInstall(mRequest.getSource());
                break;
            }
            case BridgeRequest.TYPE_OVERLAY: {
                BridgeActivity.requestOverlay(mRequest.getSource());
                break;
            }
            case BridgeRequest.TYPE_ALERT_WINDOW: {
                BridgeActivity.requestAlertWindow(mRequest.getSource());
                break;
            }
            case BridgeRequest.TYPE_NOTIFY: {
                BridgeActivity.requestNotify(mRequest.getSource());
                break;
            }
            case BridgeRequest.TYPE_NOTIFY_LISTENER: {
                BridgeActivity.requestNotificationListener(mRequest.getSource());
                break;
            }
            case BridgeRequest.TYPE_WRITE_SETTING:{
                BridgeActivity.requestWriteSetting(mRequest.getSource());
                break;
            }
        }
    }

    @Override
    public void onCallback() {
        synchronized (this) {
            mMessenger.unRegister();
            mRequest.getCallback().onCallback();
            notify();
        }
    }
}