/*
 * Copyright 2018 Zhenjie Yan
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
package com.yanzhenjie.permission.sample;

import android.app.Application;
import android.view.View;

import com.yanzhenjie.permission.sample.widget.AlertWindow;
import com.yanzhenjie.permission.sample.widget.LauncherView;

/**
 * Created by Zhenjie Yan on 2018/5/30.
 */
public class App extends Application {

    private static App _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
    }

    public static App get() {
        return _instance;
    }

    public void showLauncherView() {
        final AlertWindow alertWindow = new AlertWindow(this);
        LauncherView view = new LauncherView(this);
        view.setCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertWindow.dismiss();
            }
        });
        alertWindow.setContentView(view);
        alertWindow.show();
    }
}