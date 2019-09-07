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
package com.yanzhenjie.permission.sample.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by Zhenjie Yan on 2018/5/30.
 */
public class AlertWindow {

    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private View mContentView;

    private boolean isShowing;

    public AlertWindow(Context context) {
        this.mContext = context.getApplicationContext();
        this.create();
    }

    private void create() {
        this.mWindowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        this.mParams = new WindowManager.LayoutParams();

        mParams.packageName = mContext.getPackageName();
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        int overlay = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        int alertWindow = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mParams.type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? overlay : alertWindow;
        mParams.format = PixelFormat.RGBA_8888;

        mParams.gravity = Gravity.TOP;
    }

    /**
     * Set the view to show.
     *
     * @param layoutId target layout id.
     */
    public void setContentView(int layoutId) {
        setContentView(LayoutInflater.from(mContext).inflate(layoutId, null, false));
    }

    /**
     * Set the view to show.
     *
     * @param view target view.
     */
    public void setContentView(View view) {
        this.mContentView = view;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(-1, -2);
        } else {
            params.width = -1;
            params.height = -2;
        }
        this.mContentView.setLayoutParams(params);
    }

    /**
     * AlertWindow is displayed.
     *
     * @return true, otherwise is false.
     */
    public boolean isShowing() {
        return isShowing;
    }

    /**
     * Display the alert window.
     */
    public void show() {
        if (isShowing) {
            Log.w("AlertWindow", "AlertWindow is already displayed.");
        } else {
            isShowing = true;
            mWindowManager.addView(mContentView, mParams);
        }
    }

    /**
     * Dismiss the
     */
    public void dismiss() {
        if (!isShowing) {
            Log.w("AlertWindow", "AlertWindow is not displayed.");
        } else if (mContentView != null) {
            isShowing = false;
            mWindowManager.removeViewImmediate(mContentView);
        }
    }

}