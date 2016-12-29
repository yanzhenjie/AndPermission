/*
 * Copyright 2016 Yan Zhenjie
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
package com.yanzhenjie.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.yanzhenjie.alertdialog.AlertDialog;

/**
 * Created by Yan Zhenjie on 2016/12/27.
 */
public class SettingDialog {

    private AlertDialog.Builder mBuilder;
    private SettingService mSettingService;

    SettingDialog(@NonNull Context context, @NonNull SettingService settingService) {
        mBuilder = AlertDialog.build(context)
                .setCancelable(false)
                .setTitle(R.string.permission_title_permission_failed)
                .setMessage(R.string.permission_message_permission_failed)
                .setPositiveButton(R.string.permission_setting, mClickListener)
                .setNegativeButton(R.string.permission_cancel, mClickListener);
        this.mSettingService = settingService;
    }

    @NonNull
    public SettingDialog setTitle(@NonNull String title) {
        mBuilder.setTitle(title);
        return this;
    }

    @NonNull
    public SettingDialog setTitle(@StringRes int title) {
        mBuilder.setTitle(title);
        return this;
    }

    @NonNull
    public SettingDialog setMessage(@NonNull String message) {
        mBuilder.setMessage(message);
        return this;
    }

    @NonNull
    public SettingDialog setMessage(@StringRes int message) {
        mBuilder.setMessage(message);
        return this;
    }

    @NonNull
    public SettingDialog setNegativeButton(@NonNull String text, @Nullable DialogInterface.OnClickListener
            negativeListener) {
        mBuilder.setNegativeButton(text, negativeListener);
        return this;
    }

    @NonNull
    public SettingDialog setNegativeButton(@StringRes int text, @Nullable DialogInterface.OnClickListener
            negativeListener) {
        mBuilder.setNegativeButton(text, negativeListener);
        return this;
    }

    @NonNull
    public SettingDialog setPositiveButton(@NonNull String text) {
        mBuilder.setPositiveButton(text, mClickListener);
        return this;
    }

    @NonNull
    public SettingDialog setPositiveButton(@StringRes int text) {
        mBuilder.setPositiveButton(text, mClickListener);
        return this;
    }

    public void show() {
        mBuilder.show();
    }

    /**
     * The dialog's btn click listener.
     */
    private DialogInterface.OnClickListener mClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_NEGATIVE:
                    mSettingService.cancel();
                    break;
                case DialogInterface.BUTTON_POSITIVE:
                    mSettingService.execute();
                    break;
            }
        }
    };
}
