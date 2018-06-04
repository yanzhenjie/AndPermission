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
import java.util.Collections;
import java.util.List;

/**
 * Created by YanZhenjie on 2018/5/2.
 */
public class Runtime {

    private static final PermissionRequestFactory FACTORY;
    private static List<String> sManifestPermissions;

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
     * Check if the permissions are valid and each permission has been registered in manifest.xml.
     * This method will throw a exception if permissions are invalid or there is any permission
     * which is not registered in manifest.xml.
     *
     * @param permissions permissions which will be checked.
     */
    private void checkPermissions(String... permissions) {
        if (sManifestPermissions == null) sManifestPermissions = getManifestPermissions(mSource.getContext());

        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("Please enter at least one permission.");
        }

        for (String p : permissions) {
            if (!sManifestPermissions.contains(p)) {
                throw new IllegalStateException(String.format("The permission %1$s is not registered in manifest.xml", p));
            }
        }
    }

    /**
     * Get a list of permissions in the manifest.
     */
    private static List<String> getManifestPermissions(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions == null || permissions.length == 0) {
                throw new IllegalStateException("You did not register any permissions in the manifest.xml.");
            }
            return Collections.unmodifiableList(Arrays.asList(permissions));
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError("Package name cannot be found.");
        }
    }
}