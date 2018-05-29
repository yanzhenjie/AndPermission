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
import android.content.pm.PackageManager;
import android.media.MediaRecorder;

import java.io.File;

/**
 * Created by YanZhenjie on 2018/1/14.
 */
class RecordAudioTest implements PermissionTest {

    private Context mContext;

    RecordAudioTest(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean test() throws Throwable {
        File mTempFile = null;
        MediaRecorder mediaRecorder = new MediaRecorder();

        try {
            mTempFile = File.createTempFile("permission", "test");

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(mTempFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
            return true;
        } catch (Throwable e) {
            PackageManager packageManager = mContext.getPackageManager();
            return !packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
        } finally {
            try {
                mediaRecorder.stop();
            } catch (Exception ignored) {
            }
            try {
                mediaRecorder.release();
            } catch (Exception ignored) {
            }

            if (mTempFile != null && mTempFile.exists()) {
                mTempFile.delete();
            }
        }
    }
}