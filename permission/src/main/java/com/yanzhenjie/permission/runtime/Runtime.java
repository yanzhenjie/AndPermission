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
package com.yanzhenjie.permission.runtime;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;

import com.yanzhenjie.permission.runtime.option.RuntimeOption;
import com.yanzhenjie.permission.runtime.setting.AllRequest;
import com.yanzhenjie.permission.runtime.setting.SettingRequest;
import com.yanzhenjie.permission.source.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Zhenjie Yan on 2018/5/2.
 */
public class Runtime implements RuntimeOption {

    private static final String ADD_VOICEMAIL_MANIFEST = "android.permission.ADD_VOICEMAIL";

    private static final PermissionRequestFactory FACTORY;
    private static List<String> sAppPermissions;

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

    @Override
    public PermissionRequest permission(@NonNull String... permissions) {
        checkPermissions(permissions);
        return FACTORY.create(mSource).permission(permissions);
    }

    @Override
    public PermissionRequest permission(@NonNull String[]... groups) {
        List<String> permissionList = new ArrayList<>();
        for (String[] group : groups) {
            checkPermissions(group);
            permissionList.addAll(Arrays.asList(group));
        }
        String[] permissions = permissionList.toArray(new String[0]);
        return permission(permissions);
    }

    @Override
    public SettingRequest setting() {
        return new AllRequest(mSource);
    }

    /**
     * Check if the permissions are valid and each permission has been registered in manifest.xml. This method will
     * throw a exception if permissions are invalid or there is any permission which is not registered in manifest.xml.
     *
     * @param permissions permissions which will be checked.
     */
    private void checkPermissions(String... permissions) {
        if (sAppPermissions == null) {
            sAppPermissions = new ArrayList<>(getManifestPermissions(mSource.getContext()));
            if (sAppPermissions.contains(ADD_VOICEMAIL_MANIFEST)) {
                sAppPermissions.add(Permission.ADD_VOICEMAIL);
            }
        }

        if (permissions.length == 0) {
            throw new IllegalArgumentException("Please enter at least one permission.");
        }

        for (String target : permissions) {
            if (!sAppPermissions.contains(target)) {
                throw new IllegalStateException(String.format("The permission %1$s is not registered in manifest.xml", target));
            }
        }
    }

    /**
     * Get a list of permissions in the manifest.
     */
    public static List<String> getManifestPermissions(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions == null || permissions.length == 0) {
                throw new IllegalStateException("You did not register any permissions in the manifest.xml.");
            }
            return Arrays.asList(permissions);
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError("Package name cannot be found.");
        }
    }
}