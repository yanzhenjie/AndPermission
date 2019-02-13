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
final class RequestExecutor extends Thread {

    private final BlockingQueue<BridgeRequest> mQueue;

    public RequestExecutor(BlockingQueue<BridgeRequest> queue) {
        this.mQueue = queue;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                BridgeRequest request;
                try {
                    request = mQueue.take();
                } catch (InterruptedException e) {
                    continue;
                }
                request.execute(this);

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}