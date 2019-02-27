/*
 * Copyright © Zhenjie Yan
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
package com.yanzhenjie.permission.checker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import java.util.List;

/**
 * Created by Zhenjie Yan on 2018/1/14.
 */
class LocationCoarseTest implements PermissionTest {

    private Context mContext;

    LocationCoarseTest(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean test() throws Throwable {
        LocationManager locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        boolean networkProvider = providers.contains(LocationManager.NETWORK_PROVIDER);
        if (networkProvider) {
            return true;
        }

        PackageManager packageManager = mContext.getPackageManager();
        boolean networkHardware = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_NETWORK);
        if (!networkHardware) return true;

        return !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}