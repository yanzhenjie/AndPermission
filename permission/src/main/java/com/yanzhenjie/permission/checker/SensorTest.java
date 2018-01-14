/*
 *
 *  * Copyright © Yan Zhenjie
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package com.yanzhenjie.permission.checker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by YanZhenjie on 2018/1/14.
 */
class SensorTest implements PermissionTest, SensorEventListener {

    private SensorManager mSensorManager;

    SensorTest(SensorManager sensorManager) {
        this.mSensorManager = sensorManager;
    }

    @Override
    public void test() throws Throwable {
        Sensor sensor = null;
        try {
            sensor = mSensorManager.getDefaultSensor((Sensor.TYPE_ACCELEROMETER));
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        } finally {
            mSensorManager.unregisterListener(this, sensor);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}