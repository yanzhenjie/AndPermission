/*
 * Copyright 2018 Yan Zhenjie
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
package com.yanzhenjie.permission.runtime;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.yanzhenjie.permission.Setting;
import com.yanzhenjie.permission.runtime.setting.RuntimeSetting;
import com.yanzhenjie.permission.source.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by YanZhenjie on 2018/5/2.
 */
public class Runtime {

    private static final PermissionRequestFactory FACTORY;
    private static List<String> mRegisteredInManifestPermissions;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FACTORY = new MRequestFactory();
        } else {
            FACTORY = new LRequestFactory();
        }
    }

    public interface PermissionRequestFactory {
        /**
         * Create permission request.
         */
        PermissionRequest create(Source source);
    }

    private Source mSource;

    public Runtime(Source source) {
        this.mSource = source;
        if (mRegisteredInManifestPermissions == null) {
            mRegisteredInManifestPermissions = getRegisteredInManifestPermissions(source.getContext());
        }
    }

    /**
     * One or more permissions.
     */
    public PermissionRequest permission(String... permissions) {
        checkPermissions(permissions);
        return FACTORY.create(mSource).permission(permissions);
    }

    /**
     * One or more permission groups.
     */
    public PermissionRequest permission(String[]... groups) {
        List<String> permissionList = new ArrayList<>();
        for (String[] group : groups) {
            permissionList.addAll(Arrays.asList(group));
        }
        String[] permissions = permissionList.toArray(new String[permissionList.size()]);
        return permission(permissions);
    }

    /**
     * Permission settings.
     */
    public Setting setting() {
        return new RuntimeSetting(mSource);
    }

    /**
     * check if the permissions are valid and each permission has been registered
     * in Manifest.xml, this method will throw a certain exception if permissions
     * are invalid or there is any permission which is not registered in Manifest.xml
     *
     * @param permissions permissions which will be checked
     */
    private void checkPermissions(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("permissions is null or no one permission");
        }
        if (mRegisteredInManifestPermissions != null) {
            for (String p : permissions) {
                if (!mRegisteredInManifestPermissions.contains(p)) {
                    throw new IllegalArgumentException("the permission \"" + p + "\" is not registered in AndroidManifest.xml");
                }
            }
        }
    }

    /**
     * get permissions list which have been registered in Manifest.xml
     */
    private List<String> getRegisteredInManifestPermissions(Context context) {
        if(context == null){
            return null;
        }
        List<String> list = new ArrayList<>();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] permissions = packageInfo.requestedPermissions;
            //permissions will be null if no permissions registered in Manifest.xml
            if (permissions != null) {
                list.addAll(Arrays.asList(permissions));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}