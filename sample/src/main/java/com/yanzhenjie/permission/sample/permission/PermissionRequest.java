/*
 * Copyright © Yan Zhenjie
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
package com.yanzhenjie.permission.sample.permission;

import android.content.Context;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.List;

/**
 * <p>这就是随便封装了下，我希望起到抛砖引玉的作用，发挥你的强大脑力吧。</p>
 * Created by Yan Zhenjie on 2017/5/1.
 */
public class PermissionRequest {

    private Context mContext;
    private PermissionCallback mCallback;

    public PermissionRequest(Context context, PermissionCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    public void request() {
        AndPermission.with(mContext)
                .requestCode(110)
                .permission(Permission.PHONE, Permission.LOCATION)
                .callback(this)
                .start();
    }

    @PermissionYes(110)
    public void yes(List<String> permissions) {
        this.mCallback.onSuccessful();
    }

    @PermissionNo(110)
    public void no(List<String> permissions) {
        System.out.println(permissions.toString());
        this.mCallback.onFailure();
    }

    public interface PermissionCallback {
        void onSuccessful();

        void onFailure();
    }

}
