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
package com.yanzhenjie.permission.checker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;

import com.yanzhenjie.permission.Permission;

import java.util.List;

/**
 * Created by YanZhenjie on 2018/1/14.
 */
class LocationTest implements PermissionTest {

    private LocationManager mManager;

    LocationTest(Context context) {
        this.mManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @RequiresPermission(anyOf = {Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION})
    @Override
    public boolean test() throws Throwable {
        List<String> list = mManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            return true;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0F, new MLocationListener(mManager));
        }
        return true;
    }

    private static class MLocationListener implements LocationListener {
        private LocationManager mManager;

        public MLocationListener(LocationManager manager) {
            mManager = manager;
        }

        @Override
        public void onLocationChanged(Location location) {
            mManager.removeUpdates(this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            mManager.removeUpdates(this);
        }

        @Override
        public void onProviderEnabled(String provider) {
            mManager.removeUpdates(this);
        }

        @Override
        public void onProviderDisabled(String provider) {
            mManager.removeUpdates(this);
        }
    }
}