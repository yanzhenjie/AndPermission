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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Zhenjie Yan on 2018/1/25.
 */
class SensorActivityTest implements PermissionTest {

    private Context mContext;

    SensorActivityTest(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean test() throws Throwable {
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        try {
            Sensor heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            sensorManager.registerListener(SENSOR_EVENT_LISTENER, heartRateSensor, 3);
            sensorManager.unregisterListener(SENSOR_EVENT_LISTENER, heartRateSensor);
        } catch (Throwable e) {
            PackageManager packageManager = mContext.getPackageManager();
            return !packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR);
        }
        return true;
    }

    private static final SensorEventListener SENSOR_EVENT_LISTENER = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}