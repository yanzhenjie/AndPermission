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
class LocationFineTest implements PermissionTest {

    private Context mContext;

    LocationFineTest(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean test() throws Throwable {
        LocationManager locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        boolean gpsProvider = providers.contains(LocationManager.GPS_PROVIDER);
        boolean passiveProvider = providers.contains(LocationManager.PASSIVE_PROVIDER);
        if (gpsProvider || passiveProvider) {
            return true;
        }

        PackageManager packageManager = mContext.getPackageManager();
        boolean gpsHardware = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        if (!gpsHardware) return true;

        return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}